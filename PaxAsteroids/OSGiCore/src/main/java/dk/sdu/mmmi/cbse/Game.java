package dk.sdu.mmmi.cbse;

//LibGDX
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

//dk.sdu.mmmi
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.core.managers.GameInputProcessor;

//Other
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import dk.sdu.mmmi.cbse.common.data.entityparts.CameraPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
import java.util.ArrayList;

public class Game implements ApplicationListener {

    private BundleContext bundleContext;

    //Camera
    private static OrthographicCamera cam;
    private ShapeRenderer sr;
    private SpriteBatch batch;
    private int levelWidth = 0;
    private int levelHeight = 0;

    //private Sprite sprite, spriteMap;
    private final GameData gameData = new GameData();
    private static World world = new World();
    private static final List<IEntityProcessingService> entityProcessorList = new CopyOnWriteArrayList<>();
    private static final List<IGamePluginService> gamePluginList = new CopyOnWriteArrayList<>();
    private static List<IPostEntityProcessingService> postEntityProcessorList = new CopyOnWriteArrayList<>();
    private AssetManager assetManager;
    private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    private TiledMap map, cave;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMapRenderer tiledMapRenderer;

    //Collision
    private MapLayer objectLayer, doorLayer;
    private String blockedKey = "blocked";
    private String playerSpawn = "playerSpawn";
    private String blockedLayer = "blockedLayer";
    private TiledMapTileSet s;

    //Mouse position
    private final Vector2 mouseInWorld2D = new Vector2();
    private final Vector3 mouseInWorld3D = new Vector3();
    private Sprite mapSprite;
    private String objectKey = "objectLayer";

    //Spawn
    private MapLayer spawnLayer;

    //Background music
    private Music music_level1;

    //Get map to array
    private boolean[][] tileChecker;

    //Rectangles
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {

        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };

    public Game() {
        init();
        this.bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
    }

    private void init() {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Squakwee";
        cfg.width = 800;
        cfg.height = 400;
        cfg.useGL30 = false;
        cfg.resizable = false;
        new LwjglApplication(this, cfg);
    }

    private TiledMap loadMap() {
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("C:\\Users\\borga\\Documents\\NetBeansProjects\\Squakwee-Pax\\PaxAsteroids\\OSGiCore\\src\\main\\java\\dk\\sdu\\mmmi\\cbse\\assets\\maps\\TileMap2.tmx");
        return map;

    }

    @Override
    public void create() {
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        float w = gameData.getDisplayWidth() / 3;
        float h = gameData.getDisplayHeight() / 3;
        loadMap();

        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);

        MapProperties props = map.getProperties();
        levelWidth = props.get("width", Integer.class);
        levelHeight = props.get("height", Integer.class);
        System.out.println("Height: " + levelHeight + "\nWidth: " + levelWidth);
        cam = new OrthographicCamera(w, h);
        cam.setToOrtho(false, w, h);
        cam.translate(w, h);
        //AssetsJarFileResolver resolver = new AssetsJarFileResolver();
        assetManager = new AssetManager();
        batch = new SpriteBatch();

        music_level1 = Gdx.audio.newMusic(Gdx.files.internal("C:\\Users\\borga\\Documents\\NetBeansProjects\\Squakwee-Pax\\PaxAsteroids\\OSGiCore\\src\\main\\java\\dk\\sdu\\mmmi\\cbse\\assets\\music\\level1.ogg"));
        music_level1.setLooping(true);
        music_level1.play();
        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));

        int[][] blockedMap = new int[50][25];
        for (int[] is : blockedMap) {
            for (int i : is) {
                i = 0;
            }
        }
        objectLayer = map.getLayers().get(objectKey);
        MapObjects objects = objectLayer.getObjects();
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            int tx = (int) (rectangleObject.getRectangle().getX() / 16);
            int ty = (int) (rectangleObject.getRectangle().getY() / 16);
            int width = (int) (rectangleObject.getRectangle().getWidth() / 16);
            int height = (int) (rectangleObject.getRectangle().getHeight() / 16);
            System.out.println(tx + " " + ty);
            for (int i = 0; i <= width - 1; i++) {
                for (int j = 0; j <= height - 1; j++) {
                    blockedMap[tx + i][ty + j] = 1;
                }
            }
        }

        String line = "";
        for (int[] is : blockedMap) {
            for (int i : is) {
                line = line + i;
            }
            System.out.println(line);
            line = "";
        }
    }

    private int blockedTile() {
        ArrayList<ArrayList<Integer>> listOfTiles = new ArrayList();
        MapObjects mapObjects = map.getLayers().get(2).getObjects();
        int isBlocked = 0;
        for (MapObject mapObject : mapObjects) {
            if (mapObject.getProperties().containsKey("blocked")) {
                isBlocked = 1;
            }
        }

//        int x = mapObjects.getCount();
//        System.out.println(x);
        return isBlocked;
    }

//    public void spawnPlayer() {
//        spawnLayer = map.getLayers().get("objectLayer");
//        MapObjects objects = spawnLayer.getObjects();
//
//        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
//            if (rectangleObject.getProperties().containsKey("bed")) {
//                float[] playerSpawn = {rectangleObject.getRectangle().x, rectangleObject.getRectangle().y};
//                world.setPlayerSpawn(playerSpawn);
//                System.out.println("Player spawn");
//            }
//        }
//    }
    @Override
    public void dispose() {
        map.dispose();
        music_level1.dispose();
    }

    @Override
    public void render() {
        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameData.setDelta(Gdx.graphics.getDeltaTime());
        gameData.getKeys().update();
        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();

        update();
        draw();

        mouseInWorld3D.x = Gdx.input.getX();
        mouseInWorld3D.y = Gdx.input.getY();
        mouseInWorld3D.z = 0;

        cam.unproject(mouseInWorld3D);
        mouseInWorld2D.x = mouseInWorld3D.x;
        mouseInWorld2D.y = mouseInWorld3D.y;

//        System.out.println(mouseInWorld2D.x + " " + mouseInWorld2D.y);
        sr = new ShapeRenderer();
    }

    @Override
    public void resize(int width, int height) {
    }

    public void changeMap() {
//        Gdx.app.postRunnable(() -> {
        cave = new TmxMapLoader().load("C:\\Users\\borga\\Documents\\NetBeansProjects\\Squakwee-Pax\\PaxAsteroids\\OSGiCore\\src\\main\\java\\dk\\sdu\\mmmi\\cbse\\assets\\maps\\TileMap.tmx");
        renderer.getMap().dispose();
        renderer.setMap(cave);
        System.out.println("Hello from inside the cave");
//        });
    }

    private void update() {

        this.assetManager.update();
        this.assetManager.finishLoading();
        // Update
        for (IEntityProcessingService entityProcessorService : entityProcessorList) {
            entityProcessorService.process(gameData, world);
        }

        // Post Update
        for (IPostEntityProcessingService postEntityProcessorService : postEntityProcessorList) {
            postEntityProcessorService.process(gameData, world);

        }

        for (Entity entity : world.getEntities()) {
            if (this.sprites.containsKey(entity.getID())) {
                PositionPart position = entity.getPart(PositionPart.class);
                Sprite sprite = this.sprites.get(entity.getID());
                sprite.setPosition(position.getX(), position.getY());

                Rectangle entityRect = rectPool.obtain();
                entityRect.set(position.getX(), position.getY(), sprite.getWidth(), sprite.getHeight());

                objectLayer = map.getLayers().get(objectKey);
                MapObjects objects = objectLayer.getObjects();

                doorLayer = map.getLayers().get("objectLayer");
                MapObjects doorObject = objectLayer.getObjects();
                boolean enterCave = false;

                for (RectangleMapObject rectangleObject : doorObject.getByType(RectangleMapObject.class)) {
                    if (rectangleObject.getProperties().containsKey("door")) {
                        Rectangle rect = rectangleObject.getRectangle();
                        if (Intersector.overlaps(rect, entityRect)) {
                            enterCave = true;
                            break;
                        }
                    }
                }
                // test
                if (enterCave) {
                    int j = 1;
                    for (int i = 0; i <= j; i++) {
                        if (i == 1) {
//                            System.out.println("The door is locked! ");
                        }
                    }
                }

                CollisionPart collision = entity.getPart(CollisionPart.class);
                if (collision != null) {
                    collision.setIsColliding(false);
                    for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
                        if (rectangleObject.getProperties().containsKey(blockedKey)) {
                            Rectangle rectangle = rectangleObject.getRectangle();
                            if (Intersector.overlaps(rectangle, entityRect)) {
//                                System.out.println(entity.getID() + " colliding with object");
                                collision.setIsColliding(true);
                                break;
                            }
                        }
                    }

                    if (collision != null) {
                        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
                            if (rectangleObject.getProperties().containsKey("caveDoor")) {
                                Rectangle rect = rectangleObject.getRectangle();
                                if (Intersector.overlaps(rect, entityRect)) {
                                    enterCave = true;
                                    break;
                                }
                            }
                        }
                        if (enterCave) {
                            map.getLayers().get("topLayer").setVisible(false);
                            map.getLayers().get("bottomLayer").setVisible(false);
                            map.getLayers().get("caveLayer").setVisible(true);
                            map.getLayers().get("caveLayer2").setVisible(true);
                        }

                        for (Entity otherEntity : world.getEntities()) {
                            Sprite s = this.sprites.get(otherEntity.getID());
                            CollisionPart otherCollision = otherEntity.getPart(CollisionPart.class);
                            if (entity.getID().equals(otherEntity.getID()) || otherCollision == null || s == null) {

                            } else {
                                PositionPart p = otherEntity.getPart(PositionPart.class);
                                Rectangle otherEntityRect = rectPool.obtain();
                                otherEntityRect.set(p.getX(), p.getY(), s.getWidth(), s.getHeight());
                                if (Intersector.overlaps(otherEntityRect, entityRect)) {
//                                System.out.println(entity.getID() + " colliding with " + otherEntity.getID());
                                    collision.setIsColliding(true);
                                    break;
                                }
                            }
                        }

                        if (collision.isColliding()) {
                            position.setX(position.getOldX());
                            position.setY(position.getOldY());
                        } else {
                            position.setOldX(position.getX());
                            position.setOldY(position.getY());

                            //Get x & y for the player position
//                    System.out.println("x: " + position.getX() + " y: " + position.getY());
                        }
                    }

                    if (entity.getPart(CameraPart.class) != null) {

                        cam.position.set(sprite.getX(), sprite.getY(), 0);
                        float startX = cam.viewportWidth / 2;
                        float startY = cam.viewportHeight / 2;
                        boundary(cam, startX, startY, levelWidth * 16 - startX * 2, levelHeight * 16 - startY * 2);
                        tiledMapRenderer.setView(cam);
                        batch.setProjectionMatrix(cam.combined);
                        sr.setProjectionMatrix(cam.combined);
                        cam.update();
                    }
                }
            }
        }
    }

    public void boundary(OrthographicCamera camera, float startX, float startY, float width, float height) {
        Vector3 position = camera.position;

        if (position.x < startX) {
            position.x = startX;
        }
        if (position.y < startY) {
            position.y = startY;
        }
        if (position.x > startX + width) {
            position.x = startX + width;
        }
        if (position.y > startY + height) {
            position.y = startY + height;
        }
        camera.position.set(position);
        camera.update();
    }

    private void draw() {
        batch.begin();
        for (Entity entity : world.getEntities()) {
            if (sprites.containsKey(entity.getID())) {
                sprites.get(entity.getID()).draw(batch);

            } else {
                SpritePart spritePart = entity.getPart(SpritePart.class);
                String location = spritePart.getSpriteLocation();

                this.assetManager.load(location, Texture.class);
                this.assetManager.update();
//                System.out.println(this.assetManager.getLoadedAssets());
                while (!this.assetManager.update()) {
                    //System.out.println(this.assetManager.getProgress());
                }
//                System.out.println(this.assetManager.getLoadedAssets());
                for (String assetName : this.assetManager.getAssetNames()) {
//                    System.out.println(assetName);

                }
                if (this.assetManager.isLoaded(location, Texture.class)) {
//                    System.out.println("Sprite Loaded");
                } else {
//                    System.out.println("Sprite Not Loaded");

                }

                Sprite sprite = new Sprite(this.assetManager.get(location, Texture.class
                ));
                PositionPart position = entity.getPart(PositionPart.class
                );
                sprite.setPosition(position.getX(), position.getY());

                sprites.put(entity.getID(), sprite);
                sprite.setSize(10, 15);
            }
        }
        batch.end();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    public void addEntityProcessingService(IEntityProcessingService eps) {
        this.entityProcessorList.add(eps);
    }

    public void removeEntityProcessingService(IEntityProcessingService eps) {
        this.entityProcessorList.remove(eps);
    }

    public void addPostEntityProcessingService(IPostEntityProcessingService eps) {
        postEntityProcessorList.add(eps);
    }

    public void removePostEntityProcessingService(IPostEntityProcessingService eps) {
        postEntityProcessorList.remove(eps);
    }

    public void addGamePluginService(IGamePluginService plugin) {
        this.gamePluginList.add(plugin);
        plugin.start(gameData, world);
    }

    public void removeGamePluginService(IGamePluginService plugin) {
        this.gamePluginList.remove(plugin);
        plugin.stop(gameData, world);
    }

}

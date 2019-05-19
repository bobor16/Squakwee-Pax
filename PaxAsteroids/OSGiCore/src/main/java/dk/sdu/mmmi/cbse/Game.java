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
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.Viewport;
import dk.sdu.mmmi.cbse.common.data.entityparts.CameraPart;

public class Game implements ApplicationListener {

    private BundleContext bundleContext;

    //Camera
    private static OrthographicCamera cam;
    private Viewport viewPort;
    private ShapeRenderer sr;
    private SpriteBatch batch;
    private Texture texture;
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
    private String objectKey = "objectLayer";
    private TiledMapTileSet s;
    private Sprite mapSprite;

    //Spawn
    private MapLayer spawnLayer;

    //Background music
    private Music music_level1;

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

    @Override
    public void create() {
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        float w = gameData.getDisplayWidth() / 2;
        float h = gameData.getDisplayHeight() / 2;

        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("C:\\Users\\borga\\Documents\\NetBeansProjects\\Squakwee-Pax\\PaxAsteroids\\OSGiCore\\src\\main\\java\\dk\\sdu\\mmmi\\cbse\\assets\\maps\\TileMap2.tmx");
//        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("2");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        MapProperties props = map.getProperties();
        levelWidth = props.get("width", Integer.class);
        levelHeight = props.get("height", Integer.class);

        cam = new OrthographicCamera(w, h);
        cam.setToOrtho(false, w, h);
        cam.translate(w, h);
        //AssetsJarFileResolver resolver = new AssetsJarFileResolver();
        assetManager = new AssetManager();
        batch = new SpriteBatch();

        music_level1 = Gdx.audio.newMusic(Gdx.files.internal("C:\\Users\\borga\\Documents\\NetBeansProjects\\Squakwee-Pax\\PaxAsteroids\\OSGiCore\\src\\main\\java\\dk\\sdu\\mmmi\\cbse\\assets\\music\\level1.ogg"));
        music_level1.setLooping(true);
        music_level1.play();

//        changeMap();
        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));

    }

//    public void spawnPlayer() {
//        spawnLayer = map.getLayers().get("items");
//        MapObjects objects = spawnLayer.getObjects();
//
//        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
//            if (rectangleObject.getProperties().containsKey(playerSpawn)) {
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

                doorLayer = map.getLayers().get("doorLayer");
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
                if (enterCave) {
                    int j = 1;
                    for (int i = 0; i <= j; i++) {
                        if (i == 1) {
                            System.out.println("The door is locked! ");
                        }
                    }
                }
                boolean colliding = false;

                for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
                    if (rectangleObject.getProperties().containsKey(blockedKey)) {
                        Rectangle rectangle = rectangleObject.getRectangle();
                        if (Intersector.overlaps(rectangle, entityRect)) {
                            colliding = true;
                            break;
                        }
                    }
                }
                if (colliding) {
                    position.setX(position.getOldX());
                    position.setY(position.getOldY());
//                    System.out.println("colliding");
                } else {
                    position.setOldX(position.getX());
                    position.setOldY(position.getY());
//                    System.out.println("x: " + position.getX() + " y: " + position.getY());
//                    System.out.println("!colliding");
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
                System.out.println(this.assetManager.getLoadedAssets());
                while (!this.assetManager.update()) {
                    System.out.println(this.assetManager.getProgress());
                }
                System.out.println(this.assetManager.getLoadedAssets());
                for (String assetName : this.assetManager.getAssetNames()) {
                    System.out.println(assetName);

                }
                if (this.assetManager.isLoaded(location, Texture.class)) {
                    System.out.println("Sprite Loaded");
                } else {
                    System.out.println("Sprite Not Loaded");

                }

                Sprite sprite = new Sprite(this.assetManager.get(location, Texture.class));
                PositionPart position = entity.getPart(PositionPart.class);
                sprite.setPosition(position.getX(), position.getY());

                sprites.put(entity.getID(), sprite);
                sprite.setSize(15, 20);
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

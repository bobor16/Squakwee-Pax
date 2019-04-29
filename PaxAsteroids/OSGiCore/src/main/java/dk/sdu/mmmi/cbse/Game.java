package dk.sdu.mmmi.cbse;

//import dk.sdu.mmmi.cbse.osgimap.TileGameMap;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.core.managers.AssetsJarFileResolver;
import dk.sdu.mmmi.cbse.core.managers.AssetsJarFileResolverTest;
import dk.sdu.mmmi.cbse.core.managers.GameInputProcessor;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class Game implements ApplicationListener {

    private BundleContext bundleContext;

    private static OrthographicCamera cam;
    private ShapeRenderer sr;
    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite, spriteMap;
    private final GameData gameData = new GameData();
    private static World world = new World();
    private static final List<IEntityProcessingService> entityProcessorList = new CopyOnWriteArrayList<>();
    private static final List<IGamePluginService> gamePluginList = new CopyOnWriteArrayList<>();
    private static List<IPostEntityProcessingService> postEntityProcessorList = new CopyOnWriteArrayList<>();
    private AssetManager assetManager;
    private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
    private TiledMap map;
private OrthogonalTiledMapRenderer renderer;
    public Game() {
        init();
        this.bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
    }

    private void init() {

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Squakwee";
        cfg.width = 800;
        cfg.height = 600;
        cfg.useGL30 = false;
        cfg.resizable = false;
        new LwjglApplication(this, cfg);
    }

    @Override
    public void create() {
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("C:\\Users\\borga\\Documents\\NetBeansProjects\\Group7\\Squakwee\\OSGiMap\\src\\main\\java\\dk\\sdu\\mmmi\\cbse\\assets\\maps\\TileMap.tmx");
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        //AssetsJarFileResolver resolver = new AssetsJarFileResolver();
        assetManager = new AssetManager();
        batch = new SpriteBatch();
                renderer = new OrthogonalTiledMapRenderer(map);


        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        sr = new ShapeRenderer();

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameData.setDelta(Gdx.graphics.getDeltaTime());
        gameData.getKeys().update();
        renderer.setView(cam);
        renderer.render();

        update();
        draw();

    }

    @Override
    public void resize(int width, int height) {
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
                this.sprites.get(entity.getID()).setPosition(position.getX(), position.getY());
                
                
            }
        }
    }

    private void draw() {
        batch.begin();
        for (Entity entity : world.getEntities()) {
            if (sprites.containsKey(entity.getID())) {
                sprites.get(entity.getID()).draw(batch);
            } else {
                SpritePart spritePart = entity.getPart(SpritePart.class);
                String location = "C:/Users/borga/Documents/NetBeansProjects/Squakwee-Pax/PaxAsteroids/OSGiMap/src/main/java/dk/sdu/mmmi/cbse/assets/img/player.png";
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

                sprite.setSize(25, 30);
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

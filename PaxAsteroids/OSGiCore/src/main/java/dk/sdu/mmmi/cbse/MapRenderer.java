/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 *
 * @author Borgar Bordoy
 */
public class MapRenderer implements Screen {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private SpriteRenderer player;

    public MapRenderer() {
    }

    @Override
    public void show() {
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("C:/Users/rasmu/OneDrive/SDU/4. Semester/Project/Squakwee-Pax\\PaxAsteroids\\OSGiCore\\src\\main\\java\\dk\\sdu\\mmmi\\cbse\\assets\\maps\\TileMap.tmx");
//        map = loader.load("assets\\TileMap.tmx");

        renderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();
        player = new SpriteRenderer(new Sprite(new Texture("C:\\Users\\marti\\OneDrive - Syddansk Universitet\\Netbeans projekter\\Squakwee-Pax\\PaxAsteroids\\OSGiCore\\src\\main\\java\\dk\\sdu\\mmmi\\cbse\\assets\\img\\player.png")), (TiledMapTileLayer) map.getLayers().get(0));
        player.setPosition(5 * player.getCollisionLayer().getTileWidth(), 52 * player.getCollisionLayer().getTileHeight());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
//        camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
        
        camera.update();
        renderer.setView(camera);
        renderer.render();
        renderer.getBatch().begin();
//        player.draw(renderer.getBatch());
//        player.setSize(25, 30);
        renderer.getBatch().end();

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();

    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resume() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
//        player.getTexture().dispose();
    }

}

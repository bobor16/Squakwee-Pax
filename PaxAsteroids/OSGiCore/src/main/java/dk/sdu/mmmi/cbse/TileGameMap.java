package dk.sdu.mmmi.cbse;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TileGameMap extends Game {

    SpriteBatch batch;
    Texture img;

    @Override
    public void create() {
        setScreen(new MapRenderer());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

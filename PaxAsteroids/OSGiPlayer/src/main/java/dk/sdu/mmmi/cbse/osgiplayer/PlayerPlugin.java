package dk.sdu.mmmi.cbse.osgiplayer;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.player.Player;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PlayerPlugin implements IGamePluginService {
    private String playerID;

    public PlayerPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        Entity player = createPlayerShip(gameData);
        playerID = world.addEntity(player);
    }

    private Entity createPlayerShip(GameData gameData) {
        Entity playerShip = new Player();
        float speed = 150;
        float deceleration = 1000;
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        float radians = 3.1415f / 2;
        playerShip.add(new LifePart(3));
        playerShip.setRadius(4);
        playerShip.add(new MovingPart(speed, deceleration));
        playerShip.add(new PositionPart(x, y, radians));
        String filename = "/player.png";
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        File file = new File(PlayerPlugin.class.getResource(filename).getFile());
        System.out.println(file.getAbsolutePath());
        File file2 = new File("player2.png");
        System.out.println(file2.getAbsolutePath());
        try {
            Files.copy(file.toPath(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(PlayerPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        String spriteLocation = file2.getAbsolutePath(); /*+ "C:\\Users\\rasmu\\OneDrive\\Dokumenter\\Squakwee-Pax\\PaxAsteroids\\OSGiPlayer\\target\\OSGiPlayer-1.0-SNAPSHOT.jar!/Assets/player.png";*/
        playerShip.add(new SpritePart(spriteLocation));
        
        return playerShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.removeEntity(playerID);
    }

}

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

public class PlayerPlugin implements IGamePluginService {

    private String playerID;

    public PlayerPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        Entity player = createPlayer(gameData, world);
        playerID = world.addEntity(player);
    }

    private Entity createPlayer(GameData gameData, World world) {
        Entity playerShip = new Player();
        float speed = 150;
        float deceleration = 1000;
        float x = world.getPlayerSpawn()[0];
        float y = world.getPlayerSpawn()[1];
        float radians = 3.1415f / 2;
        playerShip.add(new LifePart(3, 69));
        playerShip.setRadius(4);
        playerShip.add(new MovingPart(speed, deceleration));
        playerShip.add(new PositionPart(x, y, radians));
        String filename = "/player.png";
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        System.out.println(PlayerPlugin.class);
        File file = new File(PlayerPlugin.class.getResource(filename).getFile());
        String spriteLocation = "C:/Users/borga/Documents/NetBeansProjects/Squakwee-Pax/PaxAsteroids/OSGiCore/src/main/java/dk/sdu/mmmi/cbse/assets/img/player.png";
        System.out.println(new File("").getAbsolutePath() + "/target");
        System.out.println(spriteLocation);/*+ "C:\\Users\\rasmu\\OneDrive\\Dokumenter\\Squakwee-Pax\\PaxAsteroids\\OSGiPlayer\\target\\OSGiPlayer-1.0-SNAPSHOT.jar!/Assets/player.png";*/
        playerShip.add(new SpritePart(spriteLocation));

        return playerShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.removeEntity(playerID);
    }

}

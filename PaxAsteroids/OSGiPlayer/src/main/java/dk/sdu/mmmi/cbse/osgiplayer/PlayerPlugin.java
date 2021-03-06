package dk.sdu.mmmi.cbse.osgiplayer;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.CameraPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
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
        System.out.println("starting player");
        Entity player = createPlayer(gameData, world);
        playerID = world.addEntity(player);
    }

    private Entity createPlayer(GameData gameData, World world) {
        Entity player = new Player();
        float speed = 150;
        float radians = 3.1415f / 2;
        player.add(new CameraPart());
        player.setRadius(4);
        player.add(new MovingPart(speed));
        // x = 317 y = 312
        player.add(new PositionPart(317, 312, radians));
        String spriteLocation = (new File("").getAbsolutePath()).replace("\\", "/") + "/bundles/OSGiPlayer_1.0.0.SNAPSHOT.jar!/player.png";
        player.add(new SpritePart(spriteLocation));
        player.add(new CollisionPart());
        
        CollisionPart c = player.getPart(CollisionPart.class);
        c.setBulletCollision(false);

        return player;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        System.out.println("stopping player");
        world.removeEntity(playerID);
    }

}

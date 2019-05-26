/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgienemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.CollisionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 *
 * @author bangp
 */
public class EnemyPlugin implements IGamePluginService {

    private String enemyID;

    public EnemyPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        System.out.println("starting enemy");

        Entity enemy = createEnemy(gameData, world);
        enemyID = world.addEntity(enemy);
    }

    private Entity createEnemy(GameData gameData, World world) {
        Entity enemy = new Enemy();
        float speed = 50;
        float deceleration = 1000;
        float x = world.getPlayerSpawn()[0];
        float y = world.getPlayerSpawn()[1];
        float radians = 3.1415f / 2;
        enemy.add(new LifePart(3, 69));
        enemy.setRadius(4);
        MovingPart moving = new MovingPart(speed, true);
        moving.setDestination(new float[]{295, 140});
        enemy.add(moving);
        enemy.add(new PositionPart(295, 140, radians));
        System.out.println(EnemyPlugin.class);
        System.out.println(FileSystems.getDefault().getPath(".").toAbsolutePath());
        String spriteLocation = (new File("").getAbsolutePath()).replace("\\", "/") + "/bundles/OSGiEnemy_1.0.0.SNAPSHOT.jar!/Chicken.png";
        System.out.println(spriteLocation);
        enemy.add(new SpritePart(spriteLocation));
        enemy.add(new CollisionPart());

        return enemy;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        System.out.println("stopping enemy");
        world.removeEntity(enemyID);
    }
}

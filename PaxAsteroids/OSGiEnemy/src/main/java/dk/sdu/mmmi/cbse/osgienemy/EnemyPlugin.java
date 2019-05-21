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
        Entity player = createEnemy(gameData, world);
        enemyID = world.addEntity(player);
    }

    private Entity createEnemy(GameData gameData, World world) {
        Entity enemy = new Enemy();
        float speed = 150;
        float deceleration = 1000;
        float x = world.getPlayerSpawn()[0];
        float y = world.getPlayerSpawn()[1];
//        float radians = 3.1415f / 2;
        enemy.add(new LifePart(40, 13));
        enemy.setRadius(4);
        enemy.add(new MovingPart(speed));
        enemy.add(new PositionPart(295, 140/*, radians*/));
        String filename = "/Chicken.png";
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        System.out.println(EnemyPlugin.class);
        File file = new File(EnemyPlugin.class.getResource(filename).getFile());
        String spriteLocation = "C:/Users/borga/Documents/NetBeansProjects/Squakwee-Pax/PaxAsteroids/OSGiEnemy/src/main/resources/Chicken.png";
        System.out.println(new File("").getAbsolutePath() + "/target");
        System.out.println(spriteLocation);/*+ "C:\\Users\\rasmu\\OneDrive\\Dokumenter\\Squakwee-Pax\\PaxAsteroids\\OSGiPlayer\\target\\OSGiPlayer-1.0-SNAPSHOT.jar!/Assets/player.png";*/
        enemy.add(new SpritePart(spriteLocation));
        enemy.add(new CollisionPart());

        return enemy;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.removeEntity(enemyID);
    }

}

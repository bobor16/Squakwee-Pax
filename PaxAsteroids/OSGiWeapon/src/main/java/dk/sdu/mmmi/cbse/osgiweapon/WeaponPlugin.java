/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgiweapon;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.player.Player;
import dk.sdu.mmmi.cbse.common.weapon.Weapon;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.io.File;

/**
 *
 * @author bangp
 */
public class WeaponPlugin implements IGamePluginService {

    private String weaponID;

    public WeaponPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        System.out.println("starting weapon");
        Entity weapon = createWeapon(gameData, world);
        weaponID = world.addEntity(weapon);
    }

    private Entity createWeapon(GameData gameData, World world) {
//        Entity player;
//        player = new Player();
//        PositionPart playerPos = player.getPart(PositionPart.class);
//        MovingPart playerMovingPart = player.getPart(MovingPart.class);
        
        Entity weaponShip = new Weapon();
        float speed = 150;
        float deceleration = 1000;
        float x = world.getPlayerSpawn()[0];
        float y = world.getPlayerSpawn()[1];
        
//        float x = playerPos.getX();
//        float y = playerPos.getY();
//        float radians = playerPos.getRadians();
//        float dt = gameData.getDelta();
//        float speed = 150;
        
//        float radians = 3.1415f / 2;
        weaponShip.add(new LifePart(3, 69));
        weaponShip.setRadius(4);
        weaponShip.add(new MovingPart(speed, deceleration));
        weaponShip.add(new PositionPart(x, y/*, radians*/));
        
//        float bx = (float) cos(radians) * shooter.getRadius() * bullet.getRadius();
//        float by = (float) sin(radians) * shooter.getRadius() * bullet.getRadius();
        
        
        String filename = "/weapon.png";
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        System.out.println(WeaponPlugin.class);
        File file = new File(WeaponPlugin.class.getResource(filename).getFile());
        String spriteLocation = "C:/Users/marti/OneDrive - Syddansk Universitet/Netbeans projekter/Squakwee-Pax/PaxAsteroids/OSGiWeapon/src/main/resources/weapon.png";
        System.out.println(new File("").getAbsolutePath() + "/target");
        System.out.println(spriteLocation);/*+ "C:\\Users\\rasmu\\OneDrive\\Dokumenter\\Squakwee-Pax\\PaxAsteroids\\OSGiPlayer\\target\\OSGiPlayer-1.0-SNAPSHOT.jar!/Assets/player.png";*/
        weaponShip.add(new SpritePart(spriteLocation));

        return weaponShip;
    }

//      @Override
//    public Entity createBullet(Entity shooter, GameData gameData) {
//        PositionPart shooterPos = shooter.getPart(PositionPart.class);
//        MovingPart shooterMovingPart = shooter.getPart(MovingPart.class);
//
//        float x = shooterPos.getX();
//        float y = shooterPos.getY();
//        float radians = shooterPos.getRadians();
//        float dt = gameData.getDelta();
//        float speed = 350;
//
//        Entity bullet = new Bullet();
//        bullet.setRadius(2);
//
//        float bx = (float) cos(radians) * shooter.getRadius() * bullet.getRadius();
//        float by = (float) sin(radians) * shooter.getRadius() * bullet.getRadius();
//        
//
//        bullet.add(new PositionPart(bx + x, by + y, radians));
//        bullet.add(new LifePart(1));
//        bullet.add(new MovingPart(0, 5000000, speed, 5));
//        bullet.add(new TimerPart(1));
//
//        bullet.setShapeX(new float[2]);
//        bullet.setShapeY(new float[2]);
//
//        return bullet;
//    }
    
    
    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        System.out.println("stopping weapon");
        world.removeEntity(weaponID);
    }
    
}


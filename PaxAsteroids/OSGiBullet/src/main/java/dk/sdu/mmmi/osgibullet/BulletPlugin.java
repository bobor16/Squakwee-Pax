/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.osgibullet;

import dk.sdu.mmmi.osgicommonbullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.TimerPart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.io.File;

/**
 *
 * @author Bruger
 */
public class BulletPlugin implements IGamePluginService {

    private String bulletID;
    
    public BulletPlugin(){
    }
    
    @Override
    public void start(GameData gameData, World world) {
        System.out.println("starting bullet");
        Entity bullet = createBullet(gameData, world);
        bulletID = world.addEntity(bullet);
    }
    
    private Entity createBullet(GameData gameData, World world) {
       
        Entity bullet = new Bullet();
        float speed = 150;
        float deceleration = 1000;
        float radians = 0;
        
        bullet.add(new LifePart(3, 69));
        bullet.setRadius(4);
        bullet.add(new MovingPart(speed, deceleration, radians));
            
        String filename = "/bullet.png";
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        System.out.println(BulletPlugin.class);
        File file = new File(BulletPlugin.class.getResource(filename).getFile());
        String spriteLocation = "C:/Users/Bruger/Documents/NetBeansProjects/Squakwee-Pax/PaxAsteroids/OSGiBullet/src/main/resources/bullet.png";
        System.out.println(new File("").getAbsolutePath() + "/target");
        System.out.println(spriteLocation);/*+ "C:\\Users\\rasmu\\OneDrive\\Dokumenter\\Squakwee-Pax\\PaxAsteroids\\OSGiPlayer\\target\\OSGiPlayer-1.0-SNAPSHOT.jar!/Assets/player.png";*/
        bullet.add(new SpritePart(spriteLocation));

        return bullet;
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("stopping bullet");
        world.removeEntity(bulletID);
    }
    
}

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
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.weapon.Weapon;
import dk.sdu.mmmi.osgicommonbullet.BulletSPI;
import java.io.File;

/**
 *
 * @author Bruger
 */
public class BulletProcessor implements IEntityProcessingService, BulletSPI {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity bullet : world.getEntities(Bullet.class)) {
            for (Entity weapon : world.getEntities(Weapon.class)) {
                PositionPart positionPart = weapon.getPart(PositionPart.class);
                MovingPart movingPart = bullet.getPart(MovingPart.class);
                float x = positionPart.getX();
                float y = positionPart.getY();
                float r = positionPart.getRadians();

                bullet.add(new PositionPart(x, y, r));

                movingPart.process(gameData, bullet);
                positionPart.process(gameData, bullet);

            }
        }
    }

    @Override
    public Entity createBullet(Entity e, GameData gameData) {
        Entity bullet = new Bullet();
        float speed = 150;
        float radians = 0;

        bullet.add(new LifePart(3, 69));
        bullet.setRadius(4);
        bullet.add(new MovingPart(speed));
        bullet.add(new PositionPart(e.getX(), e.getY(), radians));

        String filename = "/bullet.png";
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        System.out.println(BulletPlugin.class);
        File file = new File(BulletPlugin.class.getResource(filename).getFile());
        String spriteLocation = "C:/Users/borga/Documents/NetBeansProjects/Squakwee-Pax/PaxAsteroids/OSGiBullet/src/main/resources/bullet.png";
        System.out.println(new File("").getAbsolutePath() + "/target");
        System.out.println(spriteLocation);/*+ "C:\\Users\\rasmu\\OneDrive\\Dokumenter\\Squakwee-Pax\\PaxAsteroids\\OSGiPlayer\\target\\OSGiPlayer-1.0-SNAPSHOT.jar!/Assets/player.png";*/
        bullet.add(new SpritePart(spriteLocation));

        return bullet;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.osgibullet;

import dk.sdu.mmmi.osgicommonbullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.DOWN;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.LEFT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.RIGHT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.SPACE;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.UP;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.TimerPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.weapon.Weapon;
import dk.sdu.mmmi.osgicommonbullet.BulletSPI;
import java.io.File;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author Bruger
 */
public class BulletProcessor implements IEntityProcessingService, BulletSPI {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity bullet : world.getEntities(Bullet.class)) {
            PositionPart positionPart = bullet.getPart(PositionPart.class);
            MovingPart movingPart = bullet.getPart(MovingPart.class);
            TimerPart timer = bullet.getPart(TimerPart.class);

            if(gameData.getKeys().isDown(UP)){
                movingPart.setUp(true);
            }
            if(gameData.getKeys().isDown(DOWN)){
                movingPart.setDown(true);
            }
            if(gameData.getKeys().isDown(LEFT)){
                movingPart.setLeft(true);
            }
            if(gameData.getKeys().isDown(RIGHT)){
                movingPart.setRight(true);
            }
//            movingPart.setMouse(true); // FIKS KOORDINATOR I MOVING PART
            
            if (timer.getExpiration() < 0) {
                world.removeEntity(bullet);
            }
            timer.process(gameData, bullet);
            movingPart.process(gameData, bullet);
            positionPart.process(gameData, bullet);
        }
    }

    @Override
    public Entity createBullet(Entity e, GameData gameData, int mouseX, int mouseY) {

        PositionPart positionPart = e.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float r = positionPart.getRadians();
        Entity bullet = new Bullet();
        float speed = 250;
        float bx = (float) cos(r) * r * bullet.getRadius();
        float by = (float) sin(r) * r * bullet.getRadius();
        bullet.setRadius(4);
        bullet.add(new PositionPart(bx + x, by + y, r));
        bullet.add(new LifePart(3, 69));
        bullet.add(new MovingPart(speed, r));
        bullet.add(new TimerPart(1));

        String filename = "/bullet.png";
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        System.out.println(BulletPlugin.class);
        File file = new File(BulletPlugin.class.getResource(filename).getFile());
        String spriteLocation = "C:/Users/marti/OneDrive - Syddansk Universitet/Netbeans projekter/Squakwee-Pax/PaxAsteroids/OSGiBullet/src/main/resources/bullet.png";
        System.out.println(new File("").getAbsolutePath() + "/target");
        System.out.println(spriteLocation);/*+ "C:\\Users\\rasmu\\OneDrive\\Dokumenter\\Squakwee-Pax\\PaxAsteroids\\OSGiPlayer\\target\\OSGiPlayer-1.0-SNAPSHOT.jar!/Assets/player.png";*/
        bullet.add(new SpritePart(spriteLocation));

        return bullet;
    }

}

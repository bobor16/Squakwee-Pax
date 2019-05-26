/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgiweapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.weapon.Weapon;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.player.Player;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.SPACE;
import dk.sdu.mmmi.osgicommonbullet.BulletSPI;

/**
 *
 * @author Martin Sorensen
 */
public class WeaponProcessor implements IEntityProcessingService {

    private static BulletSPI bulletService;
    

    @Override
    public void process(GameData gameData, World world) {
        for (Entity weapon : world.getEntities(Weapon.class)) {
            for (Entity player : world.getEntities(Player.class)) {
                PositionPart positionPart = player.getPart(PositionPart.class);
                PositionPart positionPartWeapon = weapon.getPart(PositionPart.class);
                float x = positionPart.getX();
                float y = positionPart.getY();
                float r = positionPart.getRadians();

                weapon.add(new PositionPart(x, y, r));
                MovingPart movingPart = weapon.getPart(MovingPart.class);

                if (gameData.getKeys().j(SPACE)) {
                    System.out.println(this.bulletService);
                    world.addEntity(this.bulletService.createBullet(weapon, gameData, 0, 0));
                }
                movingPart.process(gameData, weapon);
                positionPart.process(gameData, weapon);

            }
        }
    }

    public void setBulletService(BulletSPI bulletService) {
        this.bulletService = bulletService;
        System.out.println("Registered bulletService");
        System.out.println(bulletService);
        System.out.println(this.bulletService);
    }

    public void removeBulletService(BulletSPI bulletService) {
        this.bulletService = null;
        System.out.println("Unregistered bulletService");
    }
}

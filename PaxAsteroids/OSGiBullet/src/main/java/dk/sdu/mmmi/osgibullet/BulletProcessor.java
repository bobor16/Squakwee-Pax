/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.osgibullet;

import dk.sdu.mmmi.osgicommonbullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.SPACE;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.TimerPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.weapon.Weapon;

/**
 *
 * @author Bruger
 */
public class BulletProcessor implements IEntityProcessingService{

    @Override
    public void process(GameData gameData, World world) {
        for (Entity bullet : world.getEntities(Bullet.class)) {
             for (Entity weapon : world.getEntities(Weapon.class)){
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
}
    
    
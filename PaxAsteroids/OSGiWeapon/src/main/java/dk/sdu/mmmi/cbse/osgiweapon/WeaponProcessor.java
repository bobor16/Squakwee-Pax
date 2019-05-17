/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgiweapon;

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
public class WeaponProcessor implements IEntityProcessingService{
    
    BulletSPI bulletService;
    
    @Override
    public void process(GameData gameData, World world) {
        for (Entity weapon : world.getEntities(Weapon.class)) {
             for (Entity player : world.getEntities(Player.class)){
            PositionPart positionPart = player.getPart(PositionPart.class);
            float x = positionPart.getX();
            float y = positionPart.getY();
            float r = positionPart.getRadians();
            
            weapon.add(new PositionPart(x, y, r));
            MovingPart movingPart = weapon.getPart(MovingPart.class);
            
            if (gameData.getKeys().isDown(SPACE)){
                bulletService.createBullet(weapon, gameData);
            }
            
            movingPart.process(gameData, weapon);
            positionPart.process(gameData, weapon);

        }
        }
    }
    
    public void setBulletService(BulletSPI bulletService){
        this.bulletService = bulletService;
    }
    
    public void removeBulletService(){
        this.bulletService = null;
    }
}

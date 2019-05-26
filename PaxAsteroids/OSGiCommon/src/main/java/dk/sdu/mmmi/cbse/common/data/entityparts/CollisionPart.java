/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

/**
 *
 * @author Borgar Bordoy
 */
public class CollisionPart implements EntityPart {

    private boolean isColliding;

    private boolean bulletCollision;

    @Override
    public void process(GameData gameData, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public CollisionPart() {
        this.isColliding = false;
        this.bulletCollision = true;
    }

    public boolean isBulletCollision() {
        return bulletCollision;
    }

    public void setBulletCollision(boolean bulletCollision) {
        this.bulletCollision = bulletCollision;
    }

    public boolean isColliding() {
        return isColliding;
    }

    public void setIsColliding(boolean isColliding) {
        this.isColliding = isColliding;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

/**
 *
 * @author Alexander
 */
public class SpritePart implements EntityPart {
    
    private String spriteLocation;

    public SpritePart(String spriteLocation) {
        this.spriteLocation = spriteLocation;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
    }

    public String getSpriteLocation() {
        return spriteLocation;
    }

    public void setSpriteLocation(String spriteLocation) {
        this.spriteLocation = spriteLocation;
    }
    
    
    
    
}

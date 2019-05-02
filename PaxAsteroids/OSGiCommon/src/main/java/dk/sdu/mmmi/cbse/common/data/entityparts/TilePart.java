/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.common.data.entityparts;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.TileType;

/**
 *
 * @author Borgar Bordoy
 */
public class TilePart implements EntityPart {

    private TileType type;

    public TilePart(TileType type) {
        this.type = type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    @Override
    public void process(GameData gameData, Entity entity) {
    }
}
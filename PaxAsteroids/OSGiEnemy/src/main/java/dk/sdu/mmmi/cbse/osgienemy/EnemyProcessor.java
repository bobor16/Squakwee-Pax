/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgienemy;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

/**
 *
 * @author bangp
 */
public class EnemyProcessor implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        System.out.println("Processing Enemy");
    }
    
}
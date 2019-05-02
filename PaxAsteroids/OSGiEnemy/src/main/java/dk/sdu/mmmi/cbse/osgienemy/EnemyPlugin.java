/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgienemy;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

/**
 *
 * @author bangp
 */
public class EnemyPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("Starting Enemy");
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("Stopping Enemy");
    }
    
}

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
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.io.File;


/**
 *
 * @author Bruger
 */
public class BulletPlugin implements IGamePluginService {

    private String bulletID;

    public BulletPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("starting bullet");
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("stopping bullet");
    }

}

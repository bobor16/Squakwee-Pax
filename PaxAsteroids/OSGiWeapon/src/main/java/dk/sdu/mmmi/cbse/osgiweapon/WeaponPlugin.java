/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgiweapon;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.SpritePart;
import dk.sdu.mmmi.cbse.common.weapon.Weapon;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.io.File;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 *
 * @author bangp
 */
public class WeaponPlugin implements IGamePluginService {

    private String weaponID;
    private BundleContext bundleContext;

    public BundleContext getBundleContext() {
        return bundleContext;
    }

    public WeaponPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        System.out.println("starting weapon");
        Entity weapon = createWeapon(gameData, world);
        weaponID = world.addEntity(weapon);
        this.bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

    }

    private Entity createWeapon(GameData gameData, World world) {

        Entity weapon = new Weapon();
        float speed = 150;
        //float deceleration = 1000;
        
        weapon.add(new LifePart(3, 69));
        weapon.setRadius(4);
        weapon.add(new MovingPart(speed));

        String filename = "/weapon.png";
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        System.out.println(WeaponPlugin.class);
        File file = new File(WeaponPlugin.class.getResource(filename).getFile());
        String spriteLocation = "C:/Users/rasmu/OneDrive/Dokumenter/Squakwee-Pax/PaxAsteroids/OSGiWeapon/src/main/resources/weapon.png";
        System.out.println(new File("").getAbsolutePath() + "/target");
        System.out.println(spriteLocation);/*+ "C:\\Users\\rasmu\\OneDrive\\Dokumenter\\Squakwee-Pax\\PaxAsteroids\\OSGiPlayer\\target\\OSGiPlayer-1.0-SNAPSHOT.jar!/Assets/player.png";*/
        weapon.add(new SpritePart(spriteLocation));

        return weapon;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        System.out.println("stopping weapon");
        world.removeEntity(weaponID);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgimap;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author Borgar Bordoy
 */
public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext bc) throws Exception {
//        bc.registerService(IEntityProcessingService.class, new MapChooser(), null);
        System.out.println("Hello from Map");
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
<<<<<<< HEAD

=======
        System.out.println("Map stop");
>>>>>>> master
    }

}

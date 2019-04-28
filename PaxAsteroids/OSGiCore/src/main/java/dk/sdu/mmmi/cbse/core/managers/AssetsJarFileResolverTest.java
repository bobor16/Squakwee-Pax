package dk.sdu.mmmi.cbse.core.managers;



import com.badlogic.gdx.Application;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.assets.AssetManager;

import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.utils.GdxNativesLoader;

import java.io.File;

import java.net.MalformedURLException;



/**

 *

 * @author jcs

 */

public class AssetsJarFileResolverTest {



    public AssetsJarFileResolverTest() {

    }

    public void testJarAssetManager() throws MalformedURLException {



        // SETUP

        System.out.println("testJarAssetManager");



        String jarUrl = "C:\\Users\\rasmu\\OneDrive\\Dokumenter\\Squakwee-Pax\\PaxAsteroids\\OSGiPlayer\\target\\OSGiPlayer-1.0-SNAPSHOT.jar!/Assets/player.png";



        AssetsJarFileResolver jfhr = new AssetsJarFileResolver();

        AssetManager am = new AssetManager(jfhr);



        // TEST

        am.load(jarUrl, Texture.class);

        am.finishLoading();

        Texture result = am.get(jarUrl, Texture.class);



        // ASSERTS
        if (!result.equals(null)) {
            System.out.println("SUCCESS");
        }
        else {
            System.out.println("FAILED");
        }
        
    }



}
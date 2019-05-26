/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgimap;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author rasmu
 */
public class MapPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("Set map");
        copy(getClass().getResourceAsStream("/maps/animThings.tsx"), FileSystems.getDefault().getPath("").toAbsolutePath() + "/assets/maps/animThings.tsx");
        copy(getClass().getResourceAsStream("/maps/basictiles.png"), FileSystems.getDefault().getPath("").toAbsolutePath() + "/assets/maps/basictiles.png");
        copy(getClass().getResourceAsStream("/maps/things.png"), FileSystems.getDefault().getPath("").toAbsolutePath() + "/assets/maps/things.png");
        copy(getClass().getResourceAsStream("/maps/TileMap2.tmx"), FileSystems.getDefault().getPath("").toAbsolutePath() + "/assets/maps/TileMap2.tmx");
        copy(getClass().getResourceAsStream("/maps/Tiles.tsx"), FileSystems.getDefault().getPath("").toAbsolutePath() + "/assets/maps/Tiles.tsx");
        copy(getClass().getResourceAsStream("/maps/tileSet2.tsx"), FileSystems.getDefault().getPath("").toAbsolutePath() + "/assets/maps/tileSet2.tsx");
        copy(getClass().getResourceAsStream("/maps/tileSet3.tsx"), FileSystems.getDefault().getPath("").toAbsolutePath() + "/assets/maps/tileSet3.tsx");
        copy(getClass().getResourceAsStream("/music/level1.ogg"), FileSystems.getDefault().getPath("").toAbsolutePath() + "/assets/music/level1.ogg");

        gameData.setMap(FileSystems.getDefault().getPath("").toAbsolutePath() + "/assets/maps/TileMap2.tmx");
        gameData.setMusic(FileSystems.getDefault().getPath("").toAbsolutePath() + "/assets/music/level1.ogg");
    }

    @Override
    public void stop(GameData gameData, World world) {
    }

    public static boolean copy(InputStream source, String destination) {
        boolean succeess = true;
        try {
            (new File(FileSystems.getDefault().getPath("").toAbsolutePath() + "/assets/maps")).mkdirs();
            (new File(FileSystems.getDefault().getPath("").toAbsolutePath() + "/assets/music")).mkdirs();

            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
            succeess = false;
        }

        return succeess;

    }
}

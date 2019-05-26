/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.osgienemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.DOWN;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.LEFT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.RIGHT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.UP;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.BulletPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.player.Player;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.ArrayList;

/**
 *
 * @author bangp
 */
public class EnemyProcessor implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Enemy.class)) {
            PositionPart positionPart = entity.getPart(PositionPart.class);
            MovingPart movingPart = entity.getPart(MovingPart.class);
            LifePart life = entity.getPart(LifePart.class);
            if (life.isDead()) {
                world.removeEntity(entity);
                EnemyPlugin e = new EnemyPlugin();
                world.addEntity(e.createEnemy(gameData, world));
                return;
            }
            if (world.getEntities(Player.class).size() != 0) {
                Entity player = world.getEntities(Player.class).get(0);
                PositionPart playerPosition = player.getPart(PositionPart.class);
                AStar a = new AStar(100, 50, (int) (positionPart.getX() / world.getTILESIZE()), (int) (positionPart.getY() / world.getTILESIZE()), (int) (playerPosition.getX() / world.getTILESIZE()), (int) (playerPosition.getY() / world.getTILESIZE()), world.getBlockedMap());
                ArrayList<int[]> path = a.process();
                if (path.size() > 1) {
                    float[] destination = new float[2];
                    destination[0] = path.get(path.size() - 2)[0] * world.getTILESIZE();
                    destination[1] = path.get(path.size() - 2)[1] * world.getTILESIZE();
                    movingPart.setDestination(destination);
                } else if (!path.isEmpty()) {
                    float[] destination = new float[2];
                    destination[0] = path.get(path.size() - 1)[0] * world.getTILESIZE();
                    destination[1] = path.get(path.size() - 1)[1] * world.getTILESIZE();
                    movingPart.setDestination(destination);
                }
                life.process(gameData, entity);
                movingPart.process(gameData, entity);
                positionPart.process(gameData, entity);
            }
        }
    }

}

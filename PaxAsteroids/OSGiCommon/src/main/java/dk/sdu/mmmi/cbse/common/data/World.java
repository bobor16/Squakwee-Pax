package dk.sdu.mmmi.cbse.common.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author jcs
 */
public class World {

    private int TILESIZE;
    private final Map<String, Entity> entityMap = new ConcurrentHashMap<>();
    private float[] playerSpawn = {4, 56};

    public int getTILESIZE() {
        return TILESIZE;
    }

    public void setTILESIZE(int TILESIZE) {
        this.TILESIZE = TILESIZE;
    }
    private int[][] blockedMap;

    public int[][] getBlockedMap() {
        return blockedMap;
    }

    public void setBlockedMap(int[][] blockedMap) {
        this.blockedMap = blockedMap;
    }

    public float[] getPlayerSpawn() {
        return playerSpawn;
    }

    public void setPlayerSpawn(float[] playerSpawn) {
        this.playerSpawn = playerSpawn;
    }

    public String addEntity(Entity entity) {
        entityMap.put(entity.getID(), entity);
        return entity.getID();
    }

    public void removeEntity(String entityID) {
        entityMap.remove(entityID);
    }

    public void removeEntity(Entity entity) {
        entityMap.remove(entity.getID());
    }

    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    public <E extends Entity> List<Entity> getEntities(Class<E>... entityTypes) {
        List<Entity> r = new ArrayList<>();
        for (Entity e : getEntities()) {
            for (Class<E> entityType : entityTypes) {
                if (entityType.equals(e.getClass())) {
                    r.add(e);
                }
            }
        }
        return r;
    }

    public Entity getEntity(String ID) {
        return entityMap.get(ID);
    }

}

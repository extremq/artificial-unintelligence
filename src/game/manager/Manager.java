package game.manager;

import game.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public abstract class Manager {
    public List<Entity> getEntityList() {
        return entityList;
    }

    List<Entity> entityList = new ArrayList<>();

    void onEntityDestruction(Entity entity) {

    }

    public void updateManager() {
        // Remove and update
        removeDestroyedEntities();
        innerUpdate();
    }

    abstract void innerUpdate();

    public void removeAllEntities() {
        getEntityList().clear();
    }

    private void removeDestroyedEntities() {
        List<Entity> toBeRemoved = new ArrayList<>();

        // Iterate through entities and collect the destroyed ones
        for (Entity entity: entityList) {
            // Check if entity is destroyed
            if (!entity.getDestructionStatus()) continue;

            // If it was, add it
            toBeRemoved.add(entity);
        }

        // Now remove each destroyed entity
        // This is worst case O(n^2)
        // A balanced tree would work better,
        // but I don't have time :)
        for(Entity entityToBeRemoved: toBeRemoved) {
            // Call event
            onEntityDestruction(entityToBeRemoved);
            entityList.remove(entityToBeRemoved);
        }

        toBeRemoved.clear();
    }

}

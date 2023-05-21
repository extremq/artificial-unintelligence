package game.util;

import game.entity.EntityWithHealth;

import java.awt.image.BufferedImage;

public class EntityWithHealthBuilder implements EntityBuilder {
    private EntityWithHealth entityWithHealth = new EntityWithHealth();

    @Override
    public void setPosition(Vector position) {
        entityWithHealth.setPosition(position);
    }

    @Override
    public void setSprite(BufferedImage sprite) {
        entityWithHealth.setSprite(sprite);
    }

    @Override
    public void buildRest(EntityTypes type) {
        switch (type) {
            case PLAYER -> {
                entityWithHealth.setMaximumHitPoints(100);
            }
        }
    }

    public EntityWithHealth getEntity() {
        EntityWithHealth entityWithHealth = this.entityWithHealth;
        this.entityWithHealth = new EntityWithHealth();
        return entityWithHealth;
    }
}

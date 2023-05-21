package game.util;

import game.entity.BulletEntity;

import java.awt.image.BufferedImage;

public class BulletEntityBuilder implements EntityBuilder {
    private BulletEntity bulletEntity = new BulletEntity();

    @Override
    public void setPosition(Vector position) {
        bulletEntity.setPosition(position);
    }

    @Override
    public void setSprite(BufferedImage sprite) {
        bulletEntity.setSprite(sprite);
    }

    @Override
    public void buildRest(EntityTypes type) {
        // nOTHING
    }

    public BulletEntity getEntity() {
        BulletEntity bulletEntity = this.bulletEntity;
        this.bulletEntity = new BulletEntity();
        return bulletEntity;
    }
}

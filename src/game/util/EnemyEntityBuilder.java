package game.util;

import game.entity.EnemyEntity;

import java.awt.image.BufferedImage;

public class EnemyEntityBuilder implements EntityBuilder {
    private EnemyEntity enemyEntity = new EnemyEntity();

    @Override
    public void setPosition(Vector position) {
        enemyEntity.setPosition(position);
    }

    @Override
    public void setSprite(BufferedImage sprite) {
        enemyEntity.setSprite(sprite);
    }

    @Override
    public void buildRest(EntityTypes type) {
        switch (type) {
            case ENEMY1 -> {
                enemyEntity.setAttackDamage(2);
                enemyEntity.setSpeed(50);
                enemyEntity.setMaximumHitPoints(50);
            }
            case ENEMY2 -> {
                enemyEntity.setAttackDamage(3);
                enemyEntity.setSpeed(60);
                enemyEntity.setMaximumHitPoints(200);
            }
            case ENEMY3 -> {
                enemyEntity.setAttackDamage(7);
                enemyEntity.setSpeed(70);
                enemyEntity.setMaximumHitPoints(300);
            }
        }
    }

    public EnemyEntity getEntity() {
        EnemyEntity enemyEntity = this.enemyEntity;
        this.enemyEntity = new EnemyEntity();
        return enemyEntity;
    }
}

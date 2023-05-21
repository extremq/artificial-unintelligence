package game.manager;

import game.assets.Assets;
import game.entity.BulletEntity;
import game.entity.EnemyEntity;
import game.entity.Entity;
import game.entity.TileEntity;
import game.util.Vector;

public class BulletManager extends Manager {
    private static BulletManager instance = null;

    public static synchronized BulletManager getInstance() {
        if (instance == null)
            instance = new BulletManager();

        return instance;
    }

    private BulletManager() {

    }

    public void createBullet(double speed, Vector direction, Vector start, double damage) {
        BulletEntity entity = new BulletEntity(speed, direction, start, damage);
        entity.setSprite(Assets.bulletSprite);

        entityList.add(entity);
    }

    @Override
    void innerUpdate() {
        for (Entity entity: entityList) {
            BulletEntity bulletEntity = (BulletEntity) entity;
            bulletEntity.continuePath();

            // Compute hit
            for (Entity enemyEntity: EnemyManager.getInstance().getEntityList()) {
                EnemyEntity enemy = (EnemyEntity) enemyEntity;

                // If hit
                if (Vector.distance(enemy.getPosition(), bulletEntity.getPosition()) <= enemy.getHitboxRadius() + bulletEntity.getHitboxRadius()) {
                    ((EnemyEntity) enemyEntity).subtractHitpoints(bulletEntity.getDamage());
                    bulletEntity.destroy();

                    // Do not allow for multiple hits at once.
                    break;
                }
            }

            // Compute collision with tiles
            // Tiles have a square hitbox
            for (Entity tileEntity: TileManager.getInstance().getEntityList()) {
                TileEntity tile = (TileEntity) tileEntity;

                if (tile.getSprite() == null)
                    continue;

                // Check if tile has collision enabled
                if (!tile.isCollideable())
                    continue;

                Vector tilePosition = tile.getPosition();
                double tileWidth = tile.getSprite().getWidth();
                double tileHeight = tile.getSprite().getHeight();

                // If hit
                if (bulletEntity.getPosition().x >= tilePosition.x - tileWidth / 2 && bulletEntity.getPosition().x <= tilePosition.x + tileWidth / 2 &&
                    bulletEntity.getPosition().y >= tilePosition.y - tileHeight / 2 && bulletEntity.getPosition().y <= tilePosition.y + tileHeight / 2) {
                    bulletEntity.destroy();

                    // Do not allow for multiple hits at once.
                    break;
                }
            }



            // Unload far away bullets
            if (Vector.distance(new Vector(0, 0), bulletEntity.getPosition()) > 1000) {
                bulletEntity.destroy();
            }
        }
    }
}

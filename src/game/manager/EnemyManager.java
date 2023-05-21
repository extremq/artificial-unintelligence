package game.manager;

import java.util.Random;

import game.assets.Assets;
import game.entity.EnemyEntity;
import game.entity.Entity;
import game.entity.EntityWithHealth;
import game.entity.TileEntity;
import game.util.Time;
import game.util.Vector;
import game.util.Collisions.*;

import static game.util.Collisions.solveCircleSquareCollision;

public class EnemyManager extends Manager {
    private static EnemyManager instance = null;

    public static synchronized EnemyManager getInstance() {
        if (instance == null)
            instance = new EnemyManager();

        return instance;
    }

    private EnemyManager() {

    }

    public void createWave(double amount, double health, double speed, double attackDamage) {
        while (amount-- > 0) {
            createEnemy(health, speed, attackDamage);
        }
    }

    public void reset() {
        lastElapsedTime = -10;
    }

    public void createEnemy(double health, double speed, double attackDamage) {
        EnemyEntity enemy = new EnemyEntity(health, speed, attackDamage);

        enemy.setSprite(Assets.enemySprite);

        Random random = new Random();

        Vector position = new Vector(random.nextInt(720) - 360, random.nextInt(720) - 360);
        int width = enemy.getSprite().getWidth();
        switch (random.nextInt(4)) {
            case 0 -> position.y = -360 - width;
            case 1 -> position.y = 360 + width;
            case 2 -> position.x = -360 - width;
            case 3 -> position.x = 360 + width;
        }

        enemy.setPosition(position);

        entityList.add(enemy);
    }

    @Override
    void onEntityDestruction(Entity entity) {
        PlayerManager.getInstance().addScore(1 + currentWave);
    }

    private long currentWave = 0;
    private long lastElapsedTime = -10;
    @Override
    void innerUpdate() {
        if (!PlayerManager.getInstance().playerAlive()) return;

        long elapsedTime = PlayerManager.getInstance().getElapsedTimeInSeconds();

        // Automatic creation of waves
        if (elapsedTime >= lastElapsedTime + 10) {
            lastElapsedTime = elapsedTime;
            currentWave = elapsedTime / 20 + 1;

            // Add 50 hitpoints each round start
            try {
                PlayerManager.getInstance().getPlayer().addHitpoints(50);
            }
            catch (PlayerNotCreatedException exp) {
                exp.printStackTrace();
            }

            createWave(elapsedTime + 5, 100 + 25 * currentWave, 100.0f + 1.0f * currentWave, currentWave);
        }

        try {
            Vector playerPosition = PlayerManager.getInstance().getPlayer().getPosition();
            EntityWithHealth player = PlayerManager.getInstance().getPlayer();

            for (Entity enemy : entityList) {
                EnemyEntity enemyEntity = (EnemyEntity) enemy;
                Vector newPosition = approach(enemy.getPosition(), playerPosition, enemyEntity.getSpeed());
                enemy.setPosition(newPosition);

                // If hit player, subtract some health
                if (Vector.distance(enemyEntity.getPosition(), player.getPosition()) <= enemyEntity.getHitboxRadius() + player.getHitboxRadius()) {
                    player.subtractHitpoints(enemyEntity.getAttackDamage());
                }

                // Check if enemy is in collision with a tile
                for (Entity tileEntity: TileManager.getInstance().getEntityList()) {
                    TileEntity tile = (TileEntity) tileEntity;

                    // Tiles have a square hitbox
                    if (tile.getSprite() == null)
                        continue;

                    // Check if tile has collision enabled
                    if (!tile.isCollideable())
                        continue;

                    // If hit
                    // Use utils.collision
                    enemyEntity.setPosition(solveCircleSquareCollision(enemyEntity.getPosition(), enemyEntity.getHitboxRadius(), tile.getPosition(), tile.getSprite().getWidth()));
                }
            }
        } catch (PlayerNotCreatedException exp) {
            exp.printStackTrace();
        }
    }

    Vector approach(Vector from, Vector to, double speed) {
        speed *= Time.getDeltaTimeSeconds();
        Vector direction = to.getSubtracted(from).getNormalized().getMultiplied(speed);
        return from.getAdded(direction);
    }
}

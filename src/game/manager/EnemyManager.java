package game.manager;

import java.util.Random;

import game.assets.Assets;
import game.entity.EnemyEntity;
import game.entity.Entity;
import game.entity.EntityWithHealth;
import game.entity.TileEntity;
import game.util.*;
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

    public void createWave(double amount, EntityTypes type) {
        while (amount-- > 0) {
            createEnemy(type);
        }
    }

    public void reset() {
        lastElapsedTime = -10;
    }

    public void createEnemy(EntityTypes type) {
        EnemyEntityBuilder builder = new EnemyEntityBuilder();
        EntityBuilderDirector director = new EntityBuilderDirector(builder);
        EnemyEntity enemy = null;
        switch (type) {
            case ENEMY1 -> {
                director.make(EntityTypes.ENEMY1, new Vector(0, 0));
                enemy = builder.getEntity();
            }
            case ENEMY2 -> {
                director.make(EntityTypes.ENEMY2, new Vector(0, 0));
                enemy = builder.getEntity();
            }
            case ENEMY3 -> {
                director.make(EntityTypes.ENEMY3, new Vector(0, 0));
                enemy = builder.getEntity();
            }
        }

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
    private long level = 1;

    public long getLevel() {
        return level;
    }
    @Override
    void innerUpdate() {
        if (!PlayerManager.getInstance().playerAlive()) return;

        long elapsedTime = PlayerManager.getInstance().getElapsedTimeInSeconds();

        // Automatic creation of waves
        // Random offset
        float offset = (float) (Math.random() * 5 + 1);
        if (elapsedTime >= lastElapsedTime + offset) {
            lastElapsedTime = elapsedTime;
            currentWave = elapsedTime / 20 + 1;

            // Add 50 hitpoints each round start
            try {
                PlayerManager.getInstance().getPlayer().addHitpoints(50);
            }
            catch (PlayerNotCreatedException exp) {
                exp.printStackTrace();
            }

            if (currentWave > 5 && currentWave <= 10) {
                level = 2;
                createWave(10, EntityTypes.ENEMY2);
            }
            else if (currentWave > 10) {
                level = 3;
                createWave(17, EntityTypes.ENEMY3);
            }
            else {
                level = 1;
                createWave(5, EntityTypes.ENEMY1);
            }
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

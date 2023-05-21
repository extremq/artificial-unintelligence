package game.manager;

import game.Engine;
import game.assets.Assets;
import game.entity.Entity;
import game.entity.EntityWithHealth;
import game.entity.TileEntity;
import game.util.*;

import java.awt.event.KeyEvent;
import java.io.*;

import static game.util.Collisions.solveCircleSquareCollision;

public class PlayerManager extends Manager {
    private static PlayerManager instance = null;

    /* Player and Game Variables */
    private boolean playerHasDied = false;
    private int bulletCountPlus = 0;
    private long elapsedTimeInSeconds = 0;
    private double startTime = 0;
    private long score = 0;
    private double shootingSpeedMultiplier = 1.0f;
    private double movementSpeedMultiplier = 1.0f;
    private static final double BULLET_DAMAGE = 50.0f;
    private static final double MOVEMENT_SPEED = 150.0f;
    private static final double SHOOTING_SPEED = 2.0f;
    private static final int BULLET_COUNT = 3;
    private double lastShootingTime = 0.0f;
    /* END Player and Game Variables */

    public static synchronized PlayerManager getInstance() {
        if (instance == null)
            instance = new PlayerManager();

        return instance;
    }

    private PlayerManager() {

    }

    public EntityWithHealth getPlayer() throws PlayerNotCreatedException {
        if (entityList.size() != 1) throw new PlayerNotCreatedException("Player does not exist.");

        return (EntityWithHealth) entityList.get(0);
    }

    public void createPlayer() throws PlayerAlreadyCreatedException {
        if (entityList.size() != 0) throw new PlayerAlreadyCreatedException("Cannot create more than 1 player.");

        // Reset everything
        loadStats();

        // Checker
        playerHasDied = false;

        // Create player entity and set sprite
        EntityWithHealthBuilder builder = new EntityWithHealthBuilder();
        EntityBuilderDirector director = new EntityBuilderDirector(builder);
        director.make(EntityTypes.PLAYER, new Vector(0, 0));
        EntityWithHealth entity = builder.getEntity();
        entity.setSprite(Assets.playerSprite);

        entityList.add(entity);
    }

    public boolean playerAlive() {
        return entityList.size() == 1;
    }

    public double lastSaveTime = 0.0f;

    @Override
    void onEntityDestruction(Entity entity) {
        // If player has died, trigger a sequence of events

        // Reset progress
        try {
            resetStats();
        } catch (IOException exp) {
            exp.printStackTrace();
        }

        // Switch to Score scene
        Engine.getInstance().transitionTo("Score");
    }

    @Override
    void innerUpdate() {
        if (!playerAlive()) {
            return;
        }
        elapsedTimeInSeconds = (long) ((long) Time.getSecondsElapsed() - startTime);
        // Save stats every 10 seconds
        if (Time.getSecondsElapsed() - lastSaveTime > 10.0f) {
            lastSaveTime = Time.getSecondsElapsed();

            try {
                saveStats();
            }
            catch (IOException exp) {
                exp.printStackTrace();
            }
        }

        evolvePlayer();
        computeMovementOfPlayer();
        shoot();
    }

    public long getElapsedTimeInSeconds() {
        return elapsedTimeInSeconds;
    }

    public void addScore(long amount) {
        score += amount;
    }

    public void resetStats() throws IOException {
        FileWriter fw = new FileWriter("save.txt");
        fw.write(
                "elapsedTimeInSeconds=0\n" +
                    "bulletCountPlus=0\n" +
                    "score=0\n" +
                    "shootingSpeedMultiplier=1\n" +
                    "movementSpeedMultiplier=1"
        );
        fw.close();

        // Start time
        startTime = Time.getSecondsElapsed();
        elapsedTimeInSeconds = 0;
        bulletCountPlus = 0;
        score = 0;
        shootingSpeedMultiplier = 1;
        movementSpeedMultiplier = 1;
        lastShootingTime = 0;
    }

    public void loadStats() {
        try {
            // Read save file
            File file = new File(
                    "save.txt");

            BufferedReader br
                    = new BufferedReader(new FileReader(file));

            String line;

            // Stats to be read
            double[] stats = new double[6];
            int counter = 0;
            while ((line = br.readLine()) != null) {
                stats[counter++] = Double.parseDouble(line.split("=")[1]);
            }

            br.close();

            // Load them up
            elapsedTimeInSeconds = (long) stats[0];

            // Redo time
            startTime = Time.getSecondsElapsed() - elapsedTimeInSeconds;
            bulletCountPlus = (int) stats[1];
            score = (long) stats[2];
            shootingSpeedMultiplier = stats[3];
            movementSpeedMultiplier = stats[4];
        } catch (IOException exp) {
            // File is empty actually, so make a blank save
            try {
                resetStats();
            } catch (IOException exp2) {
                exp.printStackTrace();
            }
        }
    }

    public void saveStats() throws IOException {
        FileWriter fw = new FileWriter("save.txt");
        fw.write(
                "elapsedTimeInSeconds=" + elapsedTimeInSeconds + "\n" +
                    "bulletCountPlus=" + bulletCountPlus + "\n" +
                    "score=" + score + "\n" +
                    "shootingSpeedMultiplier=" + shootingSpeedMultiplier + "\n" +
                    "movementSpeedMultiplier=" + movementSpeedMultiplier
        );
        fw.close();

    }

    public long getScore() {
        return score;
    }

    void evolvePlayer() {
        bulletCountPlus = (int) (elapsedTimeInSeconds / 25);
        shootingSpeedMultiplier = Math.min(1.0f + elapsedTimeInSeconds / 100.0f + score / 1000.0f, 4.0f);
        movementSpeedMultiplier = Math.min(1.0f + elapsedTimeInSeconds / 200.0f + score / 1000.0f, 1.25f);
    }

    void shoot() {
        if (Time.getSecondsElapsed() - lastShootingTime < 1 / (SHOOTING_SPEED * shootingSpeedMultiplier)) return;

        lastShootingTime = Time.getSecondsElapsed();

        try {
            // Compute bullets
            Vector startingVector = new Vector(0, -1);
            double step = 2 * Math.PI / (BULLET_COUNT + bulletCountPlus);

            for (int i = 0; i < BULLET_COUNT + bulletCountPlus; ++i) {
                BulletManager.getInstance().createBullet(
                        300,
                        startingVector.getRotatedBy(i * step),
                        getPlayer().getPosition(),
                        BULLET_DAMAGE
                );
            }

        } catch (PlayerNotCreatedException exp) {
            exp.printStackTrace();
        }
    }

    void computeMovementOfPlayer() {
        Vector direction = new Vector(0, 0);
        double speed = 150 * movementSpeedMultiplier * Time.getDeltaTimeSeconds();

        if (Keyboard.isKeyPressed(KeyEvent.VK_UP)) direction.add(0, -1);
        if (Keyboard.isKeyPressed(KeyEvent.VK_DOWN)) direction.add(0, 1);
        if (Keyboard.isKeyPressed(KeyEvent.VK_LEFT)) direction.add(-1, 0);
        if (Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) direction.add(1, 0);

        try {
            Vector newPosition = getPlayer().getPosition().getAdded(direction.getNormalized().getMultiplied(speed));
            if (newPosition.x < -360) {
                newPosition.x = -360;
            }
            if (newPosition.x > 360) {
                newPosition.x = 360;
            }

            if (newPosition.y < -360) {
                newPosition.y = -360;
            }
            if (newPosition.y > 360) {
                newPosition.y = 360;
            }

            // Check if player is in collision with tiles
            for (Entity tileEntity : TileManager.getInstance().getEntityList()) {
                TileEntity tile = (TileEntity) tileEntity;

                if (!tile.isCollideable())
                    continue;

                // Tiles are squares and the player is a circle
                // So we need to check if the distance between the center of the circle and the center of the square is less than the radius of the circle
                // If it is, then the player is in collision with the tile
                getPlayer().setPosition(solveCircleSquareCollision(newPosition, getPlayer().getHitboxRadius(), tile.getPosition(), tile.getSprite().getHeight()));
            }


            getPlayer().setPosition(newPosition);
        } catch (PlayerNotCreatedException exp) {
            exp.printStackTrace();
        }
    }

}

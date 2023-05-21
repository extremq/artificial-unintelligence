package game;

import game.entity.Entity;
import game.graphics.Window;
import game.manager.*;
import game.util.Keyboard;
import game.util.StateMachine;
import game.util.Time;
import game.util.Vector;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;
import java.util.List;
import java.sql.*;

public class Engine {
    private static Engine instance = null;
    private Window window = null;
    private final StateMachine gameState = new StateMachine(new String[]{"Menu", "Playing", "Score"}, "Menu");

    public static synchronized Engine getInstance() {
        if (instance == null)
            instance = new Engine();

        return instance;
    }

    public String getState() {
        return gameState.getCurrentState();
    }

    private Engine() {
        // Only getInstance should be used.
        initializeResources();
    }

    public void run(final int framesPerSecond) {
        // Determine time in nanoseconds for each frame
        final double frameTime = 1e9 / framesPerSecond;

        // Counters to keep track of flow of time
        long currentTime, previousTime = System.nanoTime();
        long startTime = System.nanoTime();
        long elapsedTime = 0;

        while (true) {
            // Game loop which first updates the game and then
            // draws the emerging graphics
            currentTime = System.nanoTime();
            elapsedTime = currentTime - startTime;

            if (currentTime - previousTime > frameTime) {
                // Convert from nanoseconds to seconds
                Time.updateSeconds(elapsedTime / 1e9);
                previousTime = currentTime;
                updateGame();

                drawGraphics();
            }
        }
    }

    public void transitionTo(String state) {
        try {
            String currentState = gameState.getCurrentState();
            if (currentState.equals("Menu")) {
                if (state.equals("Playing")) {
                    // Remove menu
                    UIManager.getInstance().removeAllEntities();

                    // Reset variables for enemies
                    EnemyManager.getInstance().reset();

                    // Started playing
                    try {
                        PlayerManager.getInstance().createPlayer();
                    }
                    catch (PlayerAlreadyCreatedException exp) {
                        exp.printStackTrace();
                    }
                    TileManager.getInstance().loadMap();
                }
            }

            if (currentState.equals("Playing")) {
                if (state.equals("Score")) {
                    UIManager.getInstance().removeAllEntities();
                    // Unload tiles, player and enemies
                    TileManager.getInstance().removeAllEntities();
                    PlayerManager.getInstance().removeAllEntities();
                    EnemyManager.getInstance().removeAllEntities();
                    BulletManager.getInstance().removeAllEntities();

                    // Show score
                    UIManager.getInstance().createScore();
                }
            }

            if (currentState.equals("Score")) {
                if (state.equals("Menu")) {
                    UIManager.getInstance().removeAllEntities();
                    UIManager.getInstance().createMenu();
                }
            }

            gameState.transitionTo(state);
        }
        catch (InvalidParameterException exp) {
            exp.printStackTrace();
        }
    }

    void initializeResources() {
        window = new Window("Game", 720, 720);
        UIManager.getInstance().createMenu();
    }

    void updateGame() {
        // Go through managers and run their code
        if (gameState.getCurrentState().equals("Playing")) {
                TileManager.getInstance().updateManager();
                PlayerManager.getInstance().updateManager();
                EnemyManager.getInstance().updateManager();
                BulletManager.getInstance().updateManager();
        }

        if (gameState.getCurrentState().equals("Score")) {
            if (Keyboard.isKeyPressed(KeyEvent.VK_P)) {
                transitionTo("Menu");
            }
        }

        if (gameState.getCurrentState().equals("Menu")) {
            if (Keyboard.isKeyPressed(KeyEvent.VK_SPACE)) {
                transitionTo("Playing");
            }
            else if (Keyboard.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                System.exit(0);
            }
        }
    }

    void drawGraphics() {
        // Create buffer strategy as needed by the java library
        BufferStrategy bufferStrategy = window.getCanvas().getBufferStrategy();

        // Needs to be created first
        if (bufferStrategy == null) {
            try {
                // Triple buffering is used
                window.getCanvas().createBufferStrategy(3);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }

        // Need to use the graphics to render sprites as per the java library
        Graphics graphics = bufferStrategy.getDrawGraphics();


        // Clear the whole screen
        graphics.clearRect(
                0, 0,
                window.getWidth(), window.getHeight()
        );

        // Draw tiles first
        drawEntities(TileManager.getInstance().getEntityList(), graphics);
        drawEntities(EnemyManager.getInstance().getEntityList(), graphics);
        drawEntities(PlayerManager.getInstance().getEntityList(), graphics);
        drawEntities(BulletManager.getInstance().getEntityList(), graphics);
        drawEntities(UIManager.getInstance().getEntityList(), graphics);

        if (PlayerManager.getInstance().playerAlive()) {
            // set the color of the text
            graphics.setColor(Color.DARK_GRAY);

            // set the font size and style
            graphics.setFont(new Font("Consolas", Font.PLAIN, 36));

            // draw the text at the specified location
            try {
                graphics.drawString("Score:" + String.valueOf(PlayerManager.getInstance().getScore()), 500, 50);
                graphics.drawString("HP:" + (int) PlayerManager.getInstance().getPlayer().getCurrentHitPoints(), 290, 50);
                graphics.drawString("Level:" + String.valueOf(EnemyManager.getInstance().getLevel()), 50, 50);
            }
            catch (PlayerNotCreatedException exception) {
                exception.printStackTrace();
            }
        }
        else if (gameState.getCurrentState().equals("Score")) {
            graphics.setColor(Color.DARK_GRAY);

            // set the font size and style
            graphics.setFont(new Font("Consolas", Font.PLAIN, 36));

            // draw the text at the specified location
            graphics.drawString(String.valueOf(PlayerManager.getInstance().getPersistentScore()), 350, 193);

            // Draw the database scores
            var scores = DatabaseManager.getInstance().getScores();
            int counter = 0;
            for (var score : scores) {
                if (score[0] == null || score[1] == null) {
                    break;
                }
                graphics.drawString(score[0], 140, 300 + counter * 50);
                graphics.drawString(String.valueOf(score[1]), 450, 300 + counter * 50);
                counter++;
            }


        }

        // Commit and display the sprites
        bufferStrategy.show();

        // Release the memory as per the java library
        graphics.dispose();
    }

    void drawEntities(List<Entity> entityList, Graphics graphics) {
        // Iterate through entities and draw them
        for (Entity entity : entityList) {
            BufferedImage sprite = entity.getSprite();

            // Translate so (0, 0) means the center of the screen
            // Also subtract sprite coordinates to center it
            Vector rectifiedPosition = entity.getPosition()
                    .getAdded(
                    new Vector(
                            window.getWidth() / 2.0f,
                            window.getHeight() / 2.0f
                    )).getSubtracted(
                    new Vector(
                            sprite.getWidth() / 2.0f,
                            sprite.getHeight() / 2.0f
                    ));

            // Draw the entity with correct coordinates
            graphics.drawImage(
                    sprite,
                    (int) rectifiedPosition.x, (int) rectifiedPosition.y,
                    sprite.getWidth(), sprite.getHeight(),
                    null
            );
        }
    }
}

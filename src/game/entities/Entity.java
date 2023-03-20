package game.entities;

import game.utils.Vector;

import java.awt.image.BufferedImage;

public class Entity {
    private Vector position;
    private BufferedImage sprite;
    private Boolean isDestroyed = false;

    public void destroy() {
        isDestroyed = true;
    }

    public boolean getDestructionStatus() {
        return isDestroyed;
    }
}

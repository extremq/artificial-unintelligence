package game.entity;

import game.util.Vector;

import java.awt.image.BufferedImage;

import static game.assets.Assets.invalidSprite;

public class Entity {
    private static long entityCounter = 0;

    public double getHitboxRadius() {
        return hitboxRadius;
    }

    public void setHitboxRadius(double hitboxRadius) {
        this.hitboxRadius = hitboxRadius;
    }

    private double hitboxRadius = 0;

    public long getId() {
        return id;
    }

    private final long id = entityCounter++;
    private final Vector position = new Vector();
    private BufferedImage sprite = invalidSprite;
    private Boolean isDestroyed = false;

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;

        // Automatically determine hitbox radius
        setHitboxRadius(sprite.getWidth() / 2.0f);
    }

    public void destroy() {
        isDestroyed = true;
    }

    public boolean getDestructionStatus() {
        return isDestroyed;
    }

    public Vector getPosition() {
        return position;
    }

    public void translatePosition(Vector offset) {
        position.add(offset);
    }

    public void setPosition(Vector newPosition) {
        position.set(newPosition);
    }
}

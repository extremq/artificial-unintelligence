package game.entity;

import game.util.Time;
import game.util.Vector;

public class BulletEntity extends Entity {
    Vector directionVector = null;
    double speed = 0;
    double damage = 0;

    public void setDirectionVector(Vector directionVector) {
        this.directionVector = directionVector;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getDamage() {
        return damage;
    }

    public void continuePath() {
        translatePosition(directionVector.getMultiplied(speed * Time.getDeltaTimeSeconds()));
    }


}

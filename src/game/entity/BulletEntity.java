package game.entity;

import game.util.Time;
import game.util.Vector;

public class BulletEntity extends Entity {
    Vector directionVector = null;
    double speed = 0;

    public double getDamage() {
        return damage;
    }

    double damage = 0;

    public BulletEntity(double bulletSpeed, Vector direction, Vector startPosition, double bulletDamage) {
        setPosition(startPosition);
        speed = bulletSpeed;
        directionVector = direction;
        damage = bulletDamage;
    }

    public void continuePath() {
        translatePosition(directionVector.getMultiplied(speed * Time.getDeltaTimeSeconds()));
    }


}

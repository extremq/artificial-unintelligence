package game.entity;

public class EnemyEntity extends EntityWithHealth {

    private double speed;
    private double attackDamage;

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAttackDamage() {
        return attackDamage;
    }
}

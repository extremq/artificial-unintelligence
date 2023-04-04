package game.entity;

public class EnemyEntity extends EntityWithHealth {
    public double getSpeed() {
        return speed;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    private final double speed;
    private final double attackDamage;
    public EnemyEntity(double hitPoints, double speed, double attackDamage) {
        super(hitPoints);
        this.speed = speed;
        this.attackDamage = attackDamage;
    }
}

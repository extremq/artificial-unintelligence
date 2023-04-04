package game.entity;

public class EntityWithHealth extends Entity {
    double maximumHitPoints = 100.0f;

    public double getCurrentHitPoints() {
        return currentHitPoints;
    }

    double currentHitPoints = 100.0f;

    public EntityWithHealth(double hitPoints) {
        super();
        maximumHitPoints = currentHitPoints = hitPoints;
    }

    public void subtractHitpoints(double amount) {
        currentHitPoints -= amount;

        if (currentHitPoints <= 0)
            destroy();
    }

    public void addHitpoints(double amount) {
        currentHitPoints += amount;

        if (currentHitPoints > maximumHitPoints)
            currentHitPoints = maximumHitPoints;
    }
}

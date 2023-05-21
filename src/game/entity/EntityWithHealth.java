package game.entity;

public class EntityWithHealth extends Entity {
    double maximumHitPoints;
    double currentHitPoints;

    public void setMaximumHitPoints(double maximumHitPoints) {
        this.maximumHitPoints = maximumHitPoints;
        this.currentHitPoints = maximumHitPoints;
    }

    public double getCurrentHitPoints() {
        return currentHitPoints;
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

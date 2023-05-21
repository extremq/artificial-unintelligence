package game.entity;

import game.assets.Assets;
import game.manager.TileType;

public class TileEntity extends Entity {
    private boolean canCollide = false;

    public boolean isCollideable() {
        return canCollide;
    }

    public void setCollideable(boolean canCollide) {
        this.canCollide = canCollide;
    }
}

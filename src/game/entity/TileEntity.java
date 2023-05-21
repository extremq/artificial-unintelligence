package game.entity;

import game.assets.Assets;
import game.manager.TileType;

public class TileEntity extends Entity {
    private boolean canCollide = false;

    public TileEntity(TileType type, boolean canCollide) {
        switch (type) {
            default -> setSprite(Assets.invalidSprite);
            case GRASS -> setSprite(Assets.grassTile);
            case STONE -> setSprite(Assets.stoneTile);
        }

        this.canCollide = canCollide;
    }

    public boolean isCollideable() {
        return canCollide;
    }
}

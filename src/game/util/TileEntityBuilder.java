package game.util;

import game.entity.TileEntity;

import java.awt.image.BufferedImage;

public class TileEntityBuilder implements EntityBuilder {
    private TileEntity tileEntity = new TileEntity();

    @Override
    public void setPosition(Vector position) {
        tileEntity.setPosition(position);
    }

    @Override
    public void setSprite(BufferedImage sprite) {
        tileEntity.setSprite(sprite);
    }

    @Override
    public void buildRest(EntityTypes type){
        switch (type) {
            case STONE_TILE -> tileEntity.setCollideable(true);
            case GRASS_TILE, ASPHALT_TILE -> tileEntity.setCollideable(false);
        }
    }

    public TileEntity getEntity() {
        TileEntity tileEntity = this.tileEntity;
        this.tileEntity = new TileEntity();
        return tileEntity;
    }
}

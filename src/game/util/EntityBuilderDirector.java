package game.util;

import game.assets.Assets;

import java.awt.image.BufferedImage;

public class EntityBuilderDirector {
    private EntityBuilder builder;

    public EntityBuilderDirector(EntityBuilder builder) {
        this.builder = builder;
    }

    public void make(EntityTypes type, Vector position) {
        switch (type) {
            case ENEMY1 -> {
                builder.setPosition(position);
                builder.setSprite(Assets.enemy1Sprite);
                builder.buildRest(EntityTypes.ENEMY1);
            }
            case ENEMY2 -> {
                builder.setPosition(position);
                builder.setSprite(Assets.enemy2Sprite);
                builder.buildRest(EntityTypes.ENEMY2);
            }
            case ENEMY3 -> {
                builder.setPosition(position);
                builder.setSprite(Assets.enemy3Sprite);
                builder.buildRest(EntityTypes.ENEMY3);
            }
            case STONE_TILE -> {
                builder.setPosition(position);
                builder.setSprite(Assets.stoneTile);
                builder.buildRest(EntityTypes.STONE_TILE);
            }
            case GRASS_TILE -> {
                builder.setPosition(position);
                builder.setSprite(Assets.grassTile);
                builder.buildRest(EntityTypes.GRASS_TILE);
            }
            case PLAYER -> {
                builder.setPosition(position);
                builder.setSprite(Assets.playerSprite);
                builder.buildRest(EntityTypes.PLAYER);
            }
            case BULLET -> {
                builder.setPosition(position);
                builder.setSprite(Assets.bulletSprite);
                builder.buildRest(EntityTypes.BULLET);
            }
            case ASPHALT_TILE -> {
                builder.setPosition(position);
                builder.setSprite(Assets.asphaltTile);
                builder.buildRest(EntityTypes.ASPHALT_TILE);
            }
        }
    }

    public void changeBuilder(EntityBuilder builder) {
        this.builder = builder;
    }
}

package game.manager;

import game.assets.Assets;
import game.entity.Entity;
import game.util.Vector;

public class TileManager extends Manager {
    private static TileManager instance = null;

    public static synchronized TileManager getInstance() {
        if (instance == null)
            instance = new TileManager();

        return instance;
    }

    private TileManager() {

    }

    public void loadMap() {
        for (int i = 0; i < 30; ++i) {
            for (int j = 0; j < 30; ++j) {
                Entity tile = createTile(TileType.GRASS);
                tile.setPosition(
                        new Vector(
                                (i + 0.5f) * tile.getSprite().getHeight() - 360,
                                (j + 0.5f) * tile.getSprite().getWidth() - 360
                        )
                );
            }
        }
    }

    public Entity createTile(TileType type) {
        Entity entity = new Entity();

        switch (type) {
            default -> entity.setSprite(Assets.invalidSprite);
            case GRASS -> entity.setSprite(Assets.grassTile);
        }

        entityList.add(entity);
        return entity;
    }

    @Override
    void innerUpdate() {
        // Tiles just exist.
    }
}

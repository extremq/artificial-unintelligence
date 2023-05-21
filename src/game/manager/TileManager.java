package game.manager;

import game.assets.Assets;
import game.entity.Entity;
import game.entity.TileEntity;
import game.util.Vector;

public class TileManager extends Manager {
    private static TileManager instance = null;
    private TileType[][] map = new TileType[30][30];

    public static synchronized TileManager getInstance() {
        if (instance == null)
            instance = new TileManager();

        return instance;
    }

    private TileManager() {

    }

    private void generateMap() {
        for (int i = 0; i < 30; ++i) {
            for (int j = 0; j < 30; ++j) {
                map[i][j] = TileType.GRASS;
            }
        }

        // Place random squares
        for (int i = 0; i < 30; ++i) {
            int x = (int) (Math.random() * 30);
            int y = (int) (Math.random() * 30);
            int size = (int) (Math.random() * 5) + 1;
            for (int j = 0; j < size; ++j) {
                for (int k = 0; k < size; ++k) {
                    if (x + j < 30 && y + k < 30) {
                        map[x + j][y + k] = TileType.STONE;
                    }
                }
            }
        }
        // make space in middle
        for (int i = 13; i < 17; ++i) {
            for (int j = 13; j < 17; ++j) {
                map[i][j] = TileType.GRASS;
            }
        }
    }

    public void loadMap() {
        generateMap();

        for (int i = 0; i < 30; ++i) {
            for (int j = 0; j < 30; ++j) {
                Entity tile = null;

                switch (map[i][j]) {
                    case INVALID, GRASS -> tile = new TileEntity(map[i][j], false);
                    case STONE -> tile = new TileEntity(map[i][j], true);
                }

                tile.setPosition(
                    new Vector(
                            (i + 0.5f) * tile.getSprite().getHeight() - 360,
                            (j + 0.5f) * tile.getSprite().getWidth() - 360
                    )
                );

                entityList.add(tile);
            }
        }
    }

    @Override
    void innerUpdate() {
        // Tiles just exist.
    }
}

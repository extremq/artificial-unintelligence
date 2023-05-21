package game.manager;

import game.assets.Assets;
import game.entity.Entity;
import game.entity.TileEntity;
import game.util.EntityBuilderDirector;
import game.util.EntityTypes;
import game.util.TileEntityBuilder;
import game.util.Vector;

import java.io.*;

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

    private void readMap() throws IOException {
        File file = new File(
                "res/map.txt");

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;

        int counter = 0;

        // Read line by line
        while ((line = br.readLine()) != null) {
            for (int i = 0; i < 30; ++i) {
                if (line.charAt(i) == '1') {
                    map[i][counter] = TileType.STONE;
                } else {
                    map[i][counter] = TileType.GRASS;
                }
            }
            ++counter;
        }

        br.close();
    }

    private void generateMap() {
        try {
            readMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap() {
        generateMap();

        for (int i = 0; i < 30; ++i) {
            for (int j = 0; j < 30; ++j) {
                Entity tile = null;

                TileEntityBuilder builder = new TileEntityBuilder();
                EntityBuilderDirector director = new EntityBuilderDirector(builder);

                switch (map[i][j]) {
                    case INVALID, GRASS -> director.make(EntityTypes.GRASS_TILE, new Vector(0, 0));
                    case STONE -> director.make(EntityTypes.STONE_TILE, new Vector(0, 0));
                }

                tile = builder.getEntity();
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

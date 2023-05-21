package game.util;

import java.awt.image.BufferedImage;

public interface EntityBuilder {
    void setPosition(Vector position);
    void setSprite(BufferedImage sprite);
    void buildRest(EntityTypes type);
}

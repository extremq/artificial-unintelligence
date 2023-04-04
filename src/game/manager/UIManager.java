package game.manager;

import game.assets.Assets;
import game.entity.Entity;
import game.entity.EntityWithHealth;

public class UIManager extends Manager {

    private static UIManager instance = null;

    public static synchronized UIManager getInstance() {
        if (instance == null)
            instance = new UIManager();

        return instance;
    }

    private UIManager() {

    }

    public void createMenu() {
        Entity entity = new Entity();
        entity.setSprite(Assets.mainMenu);

        entityList.add(entity);
    }

    @Override
    void innerUpdate() {
    }
}

package duke.gameplay.active;

import duke.gameplay.*;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.resources.AssetManager;

import static duke.level.Level.TILE_SIZE;

public class Acme extends Active implements Movable, Collidable, Physics, Updatable, SpriteRenderable {
    public Acme(int x, int y) {
        super(x, y, TILE_SIZE * 2, TILE_SIZE);
    }

    @Override
    public void update(GameplayContext context) {

    }

    @Override
    public void onCollision(Direction direction) {
    }

    @Override
    public int getVerticalAcceleration() {
        return 0;
    }

    @Override
    public void fall() {
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return SPRITE;
    }

    private static final SpriteDescriptor SPRITE = new SpriteDescriptor(AssetManager::getObjects, 83, 0, 0, 1, 2);
}

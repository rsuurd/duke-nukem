package duke.gameplay.active;

import duke.gameplay.*;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;

import static duke.level.Level.TILE_SIZE;

public class Acme extends Active implements Movable, Collidable, Physics, Updatable, SpriteRenderable {
    public Acme(int x, int y) {
        super(x, y, TILE_SIZE * 2, TILE_SIZE);
    }

    @Override
    public void update() {

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
    public Animation getAnimation() {
        return animation;
    }

    private static Animation animation = new Animation(new AnimationDescriptor(new SpriteDescriptor(AssetManager::getObjects, 83, 0, 0, 1, 2), 1, 1));
}

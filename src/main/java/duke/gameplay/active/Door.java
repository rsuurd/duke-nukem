package duke.gameplay.active;

import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Solid;
import duke.gameplay.Updatable;
import duke.gameplay.active.items.Key;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.resources.AssetManager;

public class Door extends Active implements Solid, SpriteRenderable, Updatable {
    private boolean closed;
    private Key.Type requiredKey;
    private Animation animation;

    public Door(int x, int y, Key.Type requiredKey) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        this.requiredKey = requiredKey;
        animation = new Animation(DESCRIPTOR);
        closed = true;
    }

    public boolean requiresKey(Key.Type key) {
        return requiredKey == key;
    }

    public void open() {
        closed = false;
    }

    public void update(GameplayContext context) {
        if (!closed) {
            if (animation.isFinished()) {
                destroy();
            } else {
                animation.tick();
            }
        }
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    private static final AnimationDescriptor DESCRIPTOR =
            new AnimationDescriptor(new SpriteDescriptor(AssetManager::getObjects, 128, 0, 0, 1, 1), 8, 1, AnimationDescriptor.Type.ONE_SHOT);
}

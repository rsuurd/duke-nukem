package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Solid;
import duke.gameplay.Updatable;
import duke.gameplay.active.items.Key;
import duke.gfx.*;
import duke.sfx.Sfx;

import static duke.gfx.SpriteDescriptor.OBJECTS;
import static duke.level.Level.TILE_SIZE;

public class Door extends Active implements Solid, Renderable, Updatable {
    private boolean closed;
    private Key.Type requiredKey;
    private Animation animation;

    public Door(int x, int y, int height, Key.Type requiredKey) {
        super(x, y, TILE_SIZE, height);

        this.requiredKey = requiredKey;
        animation = new Animation(DESCRIPTOR);
        closed = true;
    }

    public boolean requiresKey(Key.Type key) {
        return requiredKey == key;
    }

    public void open(GameplayContext context) {
        closed = false;
        context.getSoundManager().play(Sfx.OPEN_KEY_DOOR);
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
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        for (int offsetY = 0; offsetY < getHeight(); offsetY += TILE_SIZE) {
            spriteRenderer.render(renderer, animation.getSpriteDescriptor(), screenX, screenY + offsetY);
        }
    }

    private static final AnimationDescriptor DESCRIPTOR =
            new AnimationDescriptor(new SpriteDescriptor(OBJECTS, 128), 8, 1, AnimationDescriptor.Type.ONE_SHOT);
}

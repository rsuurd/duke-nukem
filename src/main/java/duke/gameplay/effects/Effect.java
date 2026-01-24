package duke.gameplay.effects;

import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Updatable;
import duke.gameplay.active.Wakeable;
import duke.gfx.*;
import duke.level.Level;

public class Effect extends Active implements Updatable, SpriteRenderable, Wakeable {
    private SpriteRenderable renderable;
    protected int ttl;

    protected Effect(int x, int y, SpriteDescriptor spriteDescriptor, int ttl) {
        this(x, y, new SimpleSpriteRenderable(spriteDescriptor), ttl);
    }

    protected Effect(int x, int y, AnimationDescriptor animationDescriptor) {
        this(x, y, animationDescriptor, animationDescriptor.getFrames() * animationDescriptor.getTicksPerFrame());
    }

    protected Effect(int x, int y, AnimationDescriptor animationDescriptor, int ttl) {
        this(x, y, new AnimatedSpriteRenderable(animationDescriptor), ttl);
    }

    protected Effect(int x, int y, SpriteRenderable renderable, int ttl) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        this.renderable = renderable;
        this.ttl = ttl;
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return renderable.getSpriteDescriptor();
    }

    @Override
    public boolean isVisible() {
        return renderable.isVisible();
    }

    @Override
    public void update(GameplayContext context) {
        if (renderable instanceof AnimatedSpriteRenderable animation) {
            animation.tick();
        }

        setX(getX() + getVelocityX());
        setY(getY() + getVelocityY());

        if (--ttl <= 0) {
            destroy();
        }
    }
}

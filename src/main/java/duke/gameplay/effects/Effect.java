package duke.gameplay.effects;

import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Updatable;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;

public class Effect extends Active implements Updatable, SpriteRenderable {
    private Animation animation;
    private int ttl;

    protected Effect(int x, int y, AnimationDescriptor animationDescriptor) {
        this(x, y, animationDescriptor, animationDescriptor.getFrames() * animationDescriptor.getTicksPerFrame());
    }

    protected Effect(int x, int y, AnimationDescriptor animationDescriptor, int ttl) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        animation = new Animation(animationDescriptor);
        this.ttl = ttl;

        activate();
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    @Override
    public void update(GameplayContext context) {
        animation.tick();

        setX(getX() + getVelocityX());
        setY(getY() + getVelocityY());

        if (--ttl <= 0) {
            destroy();
        }
    }
}

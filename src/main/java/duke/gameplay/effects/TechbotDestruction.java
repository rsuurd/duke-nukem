package duke.gameplay.effects;

import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Updatable;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.sfx.Sfx;

public class TechbotDestruction extends Active implements Updatable, SpriteRenderable {
    private Animation animation;

    private boolean started;

    public TechbotDestruction(int x, int y) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        animation = new Animation(DESCRIPTOR);
        started = false;
    }

    @Override
    public void update(GameplayContext context) {
        if (!started) { // TODO maybe introduce a lifecycle or sequence interface for onStart / onEnd callbacks
            context.getSoundManager().play(Sfx.SMALL_DEATH);
            started = true;
        }

        animation.tick();

        if (animation.isFinished()) {
            context.getActiveManager().spawn(EffectsFactory.createSparks(getX(), getY()));
            context.getScoreManager().score(100);

            destroy();
        }
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    private static final AnimationDescriptor DESCRIPTOR = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.ANIM, 85, 0, 0, 1, 1), 7, 1, AnimationDescriptor.Type.ONE_SHOT);
}

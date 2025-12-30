package duke.gameplay.effects;

import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.SpriteRenderable;
import duke.gameplay.Updatable;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.level.Level;

public class Effect extends Active implements Updatable, SpriteRenderable {
    private Animation animation;

    protected Effect(int x, int y, AnimationDescriptor animationDescriptor) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        animation = new Animation(animationDescriptor);
    }

    @Override
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public void update(GameplayContext context) {
        animation.tick();
    }

    @Override
    public boolean isActive() {
        return !animation.isFinished();
    }
}

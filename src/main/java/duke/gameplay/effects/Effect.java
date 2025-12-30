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

    protected Effect(int x, int y, AnimationDescriptor animationDescriptor) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        animation = new Animation(animationDescriptor);
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
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

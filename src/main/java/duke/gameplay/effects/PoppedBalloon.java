package duke.gameplay.effects;

import duke.gameplay.GameplayContext;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;

public class PoppedBalloon extends Effect {
    public PoppedBalloon(int x, int y) {
        super(x, y, POPPED_BALLOON);
    }

    @Override
    public void update(GameplayContext context) {
        super.update(context);

        if (isDestroyed()) {
            context.getActiveManager().spawn(EffectsFactory.createSmoke(getX(), getY()));
        }
    }

    private static final AnimationDescriptor POPPED_BALLOON =
            new AnimationDescriptor(new SpriteDescriptor(AssetManager::getObjects, 73, 0, 0, 1, 1), 1, 1);

}

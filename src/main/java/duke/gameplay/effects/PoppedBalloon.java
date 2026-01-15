package duke.gameplay.effects;

import duke.gameplay.GameplayContext;
import duke.gfx.SpriteDescriptor;

import static duke.gfx.SpriteDescriptor.OBJECTS;

public class PoppedBalloon extends Effect {
    public PoppedBalloon(int x, int y) {
        super(x, y, POPPED_BALLOON, 1);
    }

    @Override
    public void update(GameplayContext context) {
        super.update(context);

        if (isDestroyed()) {
            context.getActiveManager().spawn(EffectsFactory.createSmoke(getX(), getY()));
        }
    }

    private static final SpriteDescriptor POPPED_BALLOON = new SpriteDescriptor(OBJECTS, 73);
}

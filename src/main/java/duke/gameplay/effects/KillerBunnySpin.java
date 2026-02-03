package duke.gameplay.effects;

import duke.gameplay.GameplayContext;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;

import java.util.List;

import static duke.level.Level.TILE_SIZE;

public class KillerBunnySpin extends Effect {
    public KillerBunnySpin(int x, int y) {
        super(x, y, DESCRIPTOR, TTL);
    }

    @Override
    public void update(GameplayContext context) {
        super.update(context);

        if (isDestroyed()) {
            context.getScoreManager().score(5000, getX(), getY());
            context.getActiveManager().spawn(EffectsFactory.createKillerBunnySmoke(getX(), getY() - TILE_SIZE));
        }
    }

    private static final SpriteDescriptor BASE = new SpriteDescriptor(SpriteDescriptor.ANIM, 118, 0, -TILE_SIZE, 2, 1);

    private static final AnimationDescriptor DESCRIPTOR = new AnimationDescriptor(
            List.of(BASE,
                    BASE.withBaseIndex(242),
                    BASE.withBaseIndex(236),
                    BASE.withBaseIndex(244)
            ), 1, AnimationDescriptor.Type.LOOP
    );

    static final int TTL = 20;
}

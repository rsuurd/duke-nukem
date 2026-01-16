package duke.gameplay.effects;

import duke.gameplay.GameplayContext;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;

import static duke.gfx.SpriteDescriptor.ANIM;

public class Debris extends Effect {
    protected Debris(int x, int y) {
        super(x, y, DESCRIPTOR, TTL);

        setVelocityY(-2);
    }

    @Override
    public void update(GameplayContext context) {
        super.update(context);

        setVelocityY(getVelocityY() + 1);
    }

    private static final int TTL = 16;

    private static final AnimationDescriptor DESCRIPTOR = new AnimationDescriptor(new SpriteDescriptor(ANIM, 152, 0, 0, 1, 2), 2, 4, AnimationDescriptor.Type.ONE_SHOT);
}

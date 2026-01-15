package duke.gameplay.effects;

import duke.gameplay.Damaging;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;

import static duke.gfx.SpriteDescriptor.ANIM;

public class Explosion extends Effect implements Damaging {
    public Explosion(int x, int y) {
        super(x, y, EXPLOSION_ANIMATION);
    }

    private static final AnimationDescriptor EXPLOSION_ANIMATION =
            new AnimationDescriptor(new SpriteDescriptor(ANIM, 92), 6, 1);
}

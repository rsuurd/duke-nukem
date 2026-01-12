package duke.gameplay.effects;

import duke.gameplay.Damaging;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;

public class Explosion extends Effect implements Damaging {
    public Explosion(int x, int y) {
        super(x, y, EXPLOSION_ANIMATION);
    }

    private static final AnimationDescriptor EXPLOSION_ANIMATION =
            new AnimationDescriptor(new SpriteDescriptor(AssetManager::getAnim, 92, 0, 0, 1, 1), 6, 1);
}

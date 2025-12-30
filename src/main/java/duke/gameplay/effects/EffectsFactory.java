package duke.gameplay.effects;

import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;

public class EffectsFactory {
    public static Effect createSparks(int x, int y) {
        SpriteDescriptor spriteDescriptor = new SpriteDescriptor(AssetManager::getAnim, 42, 0, 0, 1, 1);
        AnimationDescriptor descriptor = new AnimationDescriptor(spriteDescriptor, 6, 1, AnimationDescriptor.Type.ONE_SHOT);

        return new Effect(x, y, descriptor);
    }
}

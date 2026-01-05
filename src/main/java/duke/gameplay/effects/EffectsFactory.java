package duke.gameplay.effects;

import duke.gameplay.Active;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;

public class EffectsFactory {
    public static Effect createSparks(int x, int y) {
        SpriteDescriptor spriteDescriptor = new SpriteDescriptor(AssetManager::getAnim, GFX_SPARKS_INDEX, 0, 0, 1, 1);
        AnimationDescriptor descriptor = new AnimationDescriptor(spriteDescriptor, 6, 1, AnimationDescriptor.Type.ONE_SHOT);

        return new Effect(x, y, descriptor);
    }

    public static Effect createDust(Active source) {
        int x = source.getX();
        int y = source.getY() + source.getHeight() / 2;

        SpriteDescriptor spriteDescriptor = new SpriteDescriptor(AssetManager::getObjects, GFX_DUST_INDEX, 0, 0, 1, 1);
        AnimationDescriptor descriptor = new AnimationDescriptor(spriteDescriptor, 5, 1, AnimationDescriptor.Type.ONE_SHOT);

        return new Effect(x, y, descriptor);
    }

    private static final int GFX_DUST_INDEX = 19;
    private static final int GFX_SPARKS_INDEX = 42;
}

package duke.gameplay.effects;

import duke.gameplay.Active;
import duke.gfx.AnimationDescriptor;
import duke.gfx.Sprite;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;

import java.util.List;
import java.util.function.Function;

public class EffectsFactory {
    public static Effect createSparks(int x, int y) {
        return createEffect(x, y, AssetManager::getAnim, GFX_SPARKS_INDEX, 6);
    }

    public static Effect createDust(Active source) {
        int x = source.getX();
        int y = source.getY() + source.getHeight() / 2;

        return createEffect(x, y, AssetManager::getObjects, GFX_DUST_INDEX, 5);
    }

    public static Effect createSmoke(int x, int y) {
        return createEffect(x, y, AssetManager::getObjects, GFX_SMOKE_INDEX, 5);
    }

    private static Effect createEffect(int x, int y, Function<AssetManager, List<Sprite>> selector, int baseIndex, int frames) {
        SpriteDescriptor spriteDescriptor = new SpriteDescriptor(selector, baseIndex, 0, 0, 1, 1);
        AnimationDescriptor descriptor = new AnimationDescriptor(spriteDescriptor, frames, 1, AnimationDescriptor.Type.ONE_SHOT);

        return new Effect(x, y, descriptor);
    }

    private static final int GFX_DUST_INDEX = 19;
    private static final int GFX_SMOKE_INDEX = 34;
    private static final int GFX_SPARKS_INDEX = 42;
}

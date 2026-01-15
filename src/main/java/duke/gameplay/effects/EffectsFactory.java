package duke.gameplay.effects;

import duke.gameplay.Active;
import duke.gameplay.Facing;
import duke.gfx.AnimationDescriptor;
import duke.gfx.Sprite;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static duke.gfx.SpriteDescriptor.ANIM;
import static duke.gfx.SpriteDescriptor.OBJECTS;

public class EffectsFactory {
    public static Effect createSparks(int x, int y) {
        return createEffect(x, y, ANIM, GFX_SPARKS_INDEX, 6);
    }

    public static Effect createDust(Active source) {
        int x = source.getX();
        int y = source.getY() + source.getHeight() / 2;

        return createEffect(x, y, OBJECTS, GFX_DUST_INDEX, 5);
    }

    public static Effect createSmoke(int x, int y) {
        return createEffect(x, y, OBJECTS, GFX_SMOKE_INDEX, 5);
    }

    public static List<Effect> createParticles(int x, int y) {
        List<Particle.Type> types = Arrays.asList(Particle.Type.values());
        Collections.shuffle(types);

        return List.of(
                new Particle(x, y - 2, -1, -4, types.get(0)),
                new Particle(x + 8, y, 1, -4, types.get(1)),
                new Particle(x, y + 8, -6, 2, types.get(2)),
                new Particle(x + 8, y + 8, 6, 0, types.get(3))
        );
    }

    public static Effect createIgnitionEffect(int x, int y, Facing facing, int ttl) {
        int baseIndex = (facing == Facing.RIGHT) ? GFX_IGNITE_RIGHT_INDEX : GFX_IGNITE_LEFT_INDEX;

        return new BlinkingEffect(x, y, new SpriteDescriptor(OBJECTS, baseIndex), ttl);
    }

    private static Effect createEffect(int x, int y, Function<AssetManager, List<Sprite>> selector, int baseIndex, int frames) {
        SpriteDescriptor spriteDescriptor = new SpriteDescriptor(selector, baseIndex);
        AnimationDescriptor descriptor = new AnimationDescriptor(spriteDescriptor, frames, 1, AnimationDescriptor.Type.ONE_SHOT);

        return new Effect(x, y, descriptor);
    }

    private static final int GFX_DUST_INDEX = 19;
    private static final int GFX_SMOKE_INDEX = 34;
    private static final int GFX_SPARKS_INDEX = 42;
    private static final int GFX_IGNITE_RIGHT_INDEX = 24;
    private static final int GFX_IGNITE_LEFT_INDEX = 29;
}

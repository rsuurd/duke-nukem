package duke.gameplay.effects;

import duke.gameplay.Active;
import duke.gameplay.Facing;
import duke.gfx.AnimationDescriptor;
import duke.gfx.Sprite;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static duke.gfx.SpriteDescriptor.ANIM;
import static duke.gfx.SpriteDescriptor.OBJECTS;
import static duke.level.Level.HALF_TILE_SIZE;
import static duke.level.Level.TILE_SIZE;

public class EffectsFactory {
    public static Effect createSparks(int x, int y) {
        return new Effect(x, y, SPARKS);
    }

    public static Effect createDust(Active source) {
        int x = source.getX();
        int y = source.getY() + source.getHeight() / 2;

        return new Effect(x, y, DUST);
    }

    public static Effect createSmoke(int x, int y) {
        return new Effect(x, y, SMOKE);
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
        SpriteDescriptor descriptor = (facing == Facing.RIGHT) ? IGNITION_RIGHT : IGNITION_LEFT;

        return new BlinkingEffect(x, y, descriptor, ttl);
    }

    public static List<Effect> createReactorHitEffect(int x, int y, int height) {
        List<Effect> effects = new ArrayList<>();

        for (int effectY = y; effectY < y + height; effectY += TILE_SIZE) {
            effects.add(new Effect(x, effectY, REACTOR_HIT, 1));
        }

        return effects;
    }

    public static Effect createTransporterActiveEffect(int x, int y, int ttl) {
        return new BlinkingEffect(x, y, TRANSPORT_ACTIVE, ttl);
    }

    public static Effect createFlash(int x, int y) {
        return new Effect(x, y, FLASH);
    }

    public static Effect createSlowFlash(int x, int y) {
        return new BlinkingEffect(x, y, SLOW_FLASH, SLOW_FLASH.getFrames() * SLOW_FLASH.getTicksPerFrame());
    }

    public static Effect createDebris(int x, int y) {
        return new Debris(x, y);
    }

    public static List<Effect> createKillerBunnySmoke(int x, int y) {
        return List.of(
                createSmoke(x + HALF_TILE_SIZE, y - 2),
                createSmoke(x + 4, y + 4),
                createSmoke(x - 4, y + HALF_TILE_SIZE),
                createSmoke(x + HALF_TILE_SIZE, y + HALF_TILE_SIZE),
                createSmoke(x - 4, y + TILE_SIZE),
                createSmoke(x + 4, y + TILE_SIZE)
        );
    }

    public static Effect createRocketIgnition(int x, int y, int ttl) {
        return new BlinkingEffect(x, y, ROCKET_IGNITION, ttl);
    }

    public static Effect createRocketBurn(int x, int y, int ttl) {
        return new BlinkingEffect(x, y, ROCKET_BURN, ttl);
    }

    private static final int GFX_DUST_INDEX = 19;
    private static final int GFX_SMOKE_INDEX = 34;
    private static final int GFX_SPARKS_INDEX = 42;
    private static final int GFX_IGNITE_RIGHT_INDEX = 24;
    private static final int GFX_IGNITE_LEFT_INDEX = 29;

    private static AnimationDescriptor create(Function<AssetManager, List<Sprite>> selector, int baseIndex, int frames) {
        SpriteDescriptor spriteDescriptor = new SpriteDescriptor(selector, baseIndex);
        return new AnimationDescriptor(spriteDescriptor, frames, 1, AnimationDescriptor.Type.ONE_SHOT);
    }

    private static final AnimationDescriptor DUST = create(OBJECTS, GFX_DUST_INDEX, 5);
    private static final AnimationDescriptor SMOKE = create(OBJECTS, GFX_SMOKE_INDEX, 5);
    private static final AnimationDescriptor SPARKS = create(ANIM, GFX_SPARKS_INDEX, 6);
    private static final AnimationDescriptor FLASH = new AnimationDescriptor(List.of(
            new SpriteDescriptor(ANIM, 215),
            new SpriteDescriptor(ANIM, 216, -8, -8, 2, 2)
    ), 1, AnimationDescriptor.Type.ONE_SHOT);

    private static final AnimationDescriptor SLOW_FLASH = FLASH.withTicksPerFrame(2);

    private static final SpriteDescriptor REACTOR_HIT = new SpriteDescriptor(SpriteDescriptor.ANIM, 265);
    private static final SpriteDescriptor IGNITION_RIGHT = new SpriteDescriptor(OBJECTS, GFX_IGNITE_RIGHT_INDEX);
    private static final SpriteDescriptor IGNITION_LEFT = IGNITION_RIGHT.withBaseIndex(GFX_IGNITE_LEFT_INDEX);
    private static final SpriteDescriptor ROCKET_IGNITION = new SpriteDescriptor(OBJECTS, 17);
    private static final SpriteDescriptor ROCKET_BURN = new SpriteDescriptor(OBJECTS, 16, 0, 0, 2, 1);

    private static final AnimationDescriptor TRANSPORT_ACTIVE = new AnimationDescriptor(new SpriteDescriptor(ANIM, 229), 2, 2);
}

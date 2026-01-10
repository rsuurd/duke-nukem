package duke.gameplay.active;

import duke.gameplay.*;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.resources.AssetManager;
import duke.sfx.Sfx;

import java.util.Map;

public class Box extends Active implements SpriteRenderable, Physics, Shootable {
    private Type type;

    public Box(int x, int y, Type type) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        this.type = type;
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        context.getActiveManager().spawn(EffectsFactory.createParticles(getX(), getY()));
        context.getSoundManager().play(Sfx.BOX_EXPLODE);

        // spawn contents at current location

        destroy();
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return DESCRIPTORS.get(type);
    }

    private static SpriteDescriptor createAnimationDescriptor(int baseIndex) {
        return new SpriteDescriptor(AssetManager::getObjects, baseIndex, 0, 0, 1, 1);
    }

    private static final Map<Type, SpriteDescriptor> DESCRIPTORS = Map.of(
            Type.GREY, createAnimationDescriptor(0),
            Type.RED, createAnimationDescriptor(101),
            Type.BLUE, createAnimationDescriptor(100)
    );

    public enum Type {
        GREY, RED, BLUE
    }
}

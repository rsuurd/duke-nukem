package duke.gameplay.active;

import duke.gameplay.*;
import duke.gameplay.active.items.Item;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.resources.AssetManager;
import duke.sfx.Sfx;

import java.util.Map;
import java.util.function.BiFunction;

public class Box extends Active implements SpriteRenderable, Physics, Shootable {
    private Type type;
    private BiFunction<Integer, Integer, Item> contents;

    public Box(int x, int y, Type type, BiFunction<Integer, Integer, Item> contents) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        this.type = type;
        this.contents = contents;
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        spawnContents(context);

        context.getActiveManager().spawn(EffectsFactory.createParticles(getX(), getY()));
        context.getSoundManager().play(Sfx.BOX_EXPLODE);

        destroy();
    }

    private void spawnContents(GameplayContext context) {
        if (contents == null) return;

        Item item = contents.apply(getX(), getY());

        if (item != null) {
            context.getActiveManager().spawn(item);
        }
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

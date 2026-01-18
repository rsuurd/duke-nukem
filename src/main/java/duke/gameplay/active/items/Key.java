package duke.gameplay.active.items;

import duke.gameplay.active.items.behavior.ItemBehaviorFactory;
import duke.gameplay.active.items.behavior.KeyBehavior;
import duke.gfx.SimpleSpriteRenderable;
import duke.gfx.SpriteDescriptor;
import duke.sfx.Sfx;

import java.util.Map;

import static duke.gfx.SpriteDescriptor.OBJECTS;

public class Key extends Item {
    public Key(int x, int y, Key.Type type) {
        super(x, y, new SimpleSpriteRenderable(DESCRIPTORS.get(type)), ItemBehaviorFactory.of(new KeyBehavior(type), ItemBehaviorFactory.bonus(1000, Sfx.GET_KEY)));
    }

    @Override
    public int getVerticalAcceleration() {
        return 0;
    }

    public enum Type {
        RED, GREEN, BLUE, MAGENTA
    }

    private static final Map<Type, SpriteDescriptor> DESCRIPTORS = Map.of(
            Type.RED, new SpriteDescriptor(OBJECTS, 124),
            Type.GREEN, new SpriteDescriptor(OBJECTS, 125),
            Type.BLUE, new SpriteDescriptor(OBJECTS, 126),
            Type.MAGENTA, new SpriteDescriptor(OBJECTS, 127)
    );
}

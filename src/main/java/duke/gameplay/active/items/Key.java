package duke.gameplay.active.items;

import duke.gfx.SimpleSpriteRenderable;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;
import duke.sfx.Sfx;

import java.util.Map;

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
            Type.RED, new SpriteDescriptor(AssetManager::getObjects, 124, 0, 0, 1, 1),
            Type.GREEN, new SpriteDescriptor(AssetManager::getObjects, 125, 0, 0, 1, 1),
            Type.BLUE, new SpriteDescriptor(AssetManager::getObjects, 126, 0, 0, 1, 1),
            Type.MAGENTA, new SpriteDescriptor(AssetManager::getObjects, 127, 0, 0, 1, 1)
    );
}

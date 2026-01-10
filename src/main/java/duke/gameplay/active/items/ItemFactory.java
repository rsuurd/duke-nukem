package duke.gameplay.active.items;

import duke.gameplay.active.items.BonusItemBehavior.RandomBonusItemBehavior;
import duke.gfx.AnimatedSpriteRenderable;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SimpleSpriteRenderable;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;

public class ItemFactory {
    public static Item createFootball(int x, int y) {
        return new Item(x, y, new SimpleSpriteRenderable(createItemSpriteDescriptor(FOOTBALL_SPRITE_INDEX)), new BonusItemBehavior(100));
    }

    public static Item createFloppy(int x, int y) {
        return new Item(x, y, new SimpleSpriteRenderable(createItemSpriteDescriptor(FLOPPY_SPRITE_INDEX)), new BonusItemBehavior(5000));
    }

    public static Item createJoystick(int x, int y) {
        return new Item(x, y, new SimpleSpriteRenderable(createItemSpriteDescriptor(JOYSTICK_SPRITE_INDEX)), new BonusItemBehavior(2000));
    }

    public static Item createFlag(int x, int y) {
        return new Item(x, y, new AnimatedSpriteRenderable(createItemAnimationDescriptor(FLAG_SPRITE_INDEX)), new RandomBonusItemBehavior());
    }

    public static Item createRadio(int x, int y) {
        return new Item(x, y, new AnimatedSpriteRenderable(createItemAnimationDescriptor(RADIO_SPRITE_INDEX)), new RandomBonusItemBehavior());
    }

    private static SpriteDescriptor createItemSpriteDescriptor(int index) {
        return new SpriteDescriptor(AssetManager::getObjects, index, 0, 0, 1, 1);
    }

    private static AnimationDescriptor createItemAnimationDescriptor(int index) {
        SpriteDescriptor spriteDescriptor = createItemSpriteDescriptor(index);

        return new AnimationDescriptor(spriteDescriptor, 3, 1);
    }

    private static final int FOOTBALL_SPRITE_INDEX = 58;
    private static final int JOYSTICK_SPRITE_INDEX = 59;
    private static final int FLOPPY_SPRITE_INDEX = 60;
    private static final int FLAG_SPRITE_INDEX = 97;
    private static final int RADIO_SPRITE_INDEX = 102;
}

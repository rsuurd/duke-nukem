package duke.gameplay.active.items;

import duke.gameplay.active.items.BonusItemBehavior.RandomBonusItemBehavior;
import duke.gfx.*;
import duke.resources.AssetManager;

import java.util.List;
import java.util.function.Function;

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

    public static Item createSoda(int x, int y) {
        AnimationDescriptor itemAnimationDescriptor = new AnimationDescriptor(createItemSpriteDescriptor(AssetManager::getAnim, SODA_SPRITE_INDEX), 4, 4);

        return new Soda(x, y, new AnimatedSpriteRenderable(itemAnimationDescriptor), new HealthBehavior(1, 200));
    }

    public static Item createFizzingSoda(int x, int y) {
        AnimationDescriptor itemAnimationDescriptor = new AnimationDescriptor(createItemSpriteDescriptor(AssetManager::getAnim, FIZZING_SODA_SPRITE_INDEX), 4, 4);

        return new FizzingSoda(x, y, new AnimatedSpriteRenderable(itemAnimationDescriptor), new BonusItemBehavior(1000));
    }

    public static Item createTurkeyLeg(int x, int y) {
        return new TurkeyLeg(x, y, new SimpleSpriteRenderable(createItemSpriteDescriptor(TURKEY_LEG_SPRITE_INDEX)), new HealthBehavior(1, 100));
    }

    public static Item createTurkey(int x, int y) {
        return new Item(x, y, new SimpleSpriteRenderable(createItemSpriteDescriptor(TURKEY_SPRITE_INDEX)), new HealthBehavior(2, 200));
    }

    private static SpriteDescriptor createItemSpriteDescriptor(int index) {
        return createItemSpriteDescriptor(AssetManager::getObjects, index);
    }

    private static SpriteDescriptor createItemSpriteDescriptor(Function<AssetManager, List<Sprite>> selector, int index) {
        return new SpriteDescriptor(selector, index, 0, 0, 1, 1);
    }

    private static AnimationDescriptor createItemAnimationDescriptor(int index) {
        return createItemAnimationDescriptor(AssetManager::getObjects, index);
    }

    private static AnimationDescriptor createItemAnimationDescriptor(Function<AssetManager, List<Sprite>> selector, int index) {
        SpriteDescriptor spriteDescriptor = createItemSpriteDescriptor(selector, index);

        return new AnimationDescriptor(spriteDescriptor, 3, 1);
    }

    private static final int TURKEY_LEG_SPRITE_INDEX = 44;
    private static final int TURKEY_SPRITE_INDEX = 45;
    private static final int FOOTBALL_SPRITE_INDEX = 58;
    private static final int JOYSTICK_SPRITE_INDEX = 59;
    private static final int FLOPPY_SPRITE_INDEX = 60;
    private static final int FLAG_SPRITE_INDEX = 97;
    private static final int RADIO_SPRITE_INDEX = 102;

    private static final int SODA_SPRITE_INDEX = 132;
    private static final int FIZZING_SODA_SPRITE_INDEX = 136;
}

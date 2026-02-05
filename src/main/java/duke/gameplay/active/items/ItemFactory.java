package duke.gameplay.active.items;

import duke.dialog.Hints;
import duke.gameplay.active.items.behavior.CharacterBehavior;
import duke.gameplay.active.items.behavior.EquipmentBehavior;
import duke.gameplay.active.items.behavior.FirepowerUpgradeBehavior;
import duke.gameplay.player.Inventory;
import duke.gfx.*;
import duke.resources.AssetManager;
import duke.sfx.Sfx;

import java.util.List;
import java.util.function.Function;

import static duke.gameplay.active.items.behavior.ItemBehaviorFactory.*;
import static duke.gameplay.player.PlayerHealth.MAX_HEALTH;
import static duke.gfx.SpriteDescriptor.ANIM;
import static duke.gfx.SpriteDescriptor.OBJECTS;

public class ItemFactory {
    public static Key createKey(int x, int y, Key.Type type) {
        return new Key(x, y, type);
    }

    public static Item createFootball(int x, int y) {
        return new Item(x, y, new SimpleSpriteRenderable(createItemSpriteDescriptor(FOOTBALL_SPRITE_INDEX)), bonus(100, Sfx.GET_BONUS_OBJECT));
    }

    public static Item createFloppy(int x, int y) {
        return new Item(x, y, new SimpleSpriteRenderable(createItemSpriteDescriptor(FLOPPY_SPRITE_INDEX)), bonus(5000, Sfx.GET_BONUS_OBJECT));
    }

    public static Item createJoystick(int x, int y) {
        return new Item(x, y, new SimpleSpriteRenderable(createItemSpriteDescriptor(JOYSTICK_SPRITE_INDEX)), bonus(2000, Sfx.GET_BONUS_OBJECT));
    }

    public static Item createFlag(int x, int y) {
        return new Item(x, y, new AnimatedSpriteRenderable(createItemAnimationDescriptor(FLAG_SPRITE_INDEX)), randomBonus());
    }

    public static Item createRadio(int x, int y) {
        return new Item(x, y, new AnimatedSpriteRenderable(createItemAnimationDescriptor(RADIO_SPRITE_INDEX)), randomBonus());
    }

    public static Item createSoda(int x, int y) {
        AnimationDescriptor itemAnimationDescriptor = new AnimationDescriptor(createItemSpriteDescriptor(ANIM, SODA_SPRITE_INDEX), 4, 4);

        return new Soda(x, y, new AnimatedSpriteRenderable(itemAnimationDescriptor), health(1, 200, Sfx.GET_FOOD_ITEM, Hints.Hint.SODA));
    }

    public static Item createFizzingSoda(int x, int y) {
        AnimationDescriptor itemAnimationDescriptor = new AnimationDescriptor(createItemSpriteDescriptor(ANIM, FIZZING_SODA_SPRITE_INDEX), 4, 4);

        return new FizzingSoda(x, y, new AnimatedSpriteRenderable(itemAnimationDescriptor), bonus(1000, Sfx.GET_BONUS_OBJECT));
    }

    public static Item createTurkeyLeg(int x, int y) {
        return new TurkeyLeg(x, y, new SimpleSpriteRenderable(createItemSpriteDescriptor(TURKEY_LEG_SPRITE_INDEX)), health(1, 100, Sfx.GET_FOOD_ITEM, Hints.Hint.TURKEY));
    }

    public static Item createTurkey(int x, int y) {
        return new Item(x, y, new SimpleSpriteRenderable(createItemSpriteDescriptor(TURKEY_SPRITE_INDEX)), health(2, 200, Sfx.GET_FOOD_ITEM, Hints.Hint.TURKEY));
    }

    public static Item createNuclearMolecule(int x, int y) {
        SpriteDescriptor spriteDescriptor = createItemSpriteDescriptor(NUCLEAR_MOLECULE_SPRITE_INDEX);
        AnimationDescriptor animationDescriptor = new AnimationDescriptor(spriteDescriptor, 9, 1);

        return new Item(x, y, new AnimatedSpriteRenderable(animationDescriptor), health(MAX_HEALTH, 1000, Sfx.GET_POWER_UP, Hints.Hint.NUCLEAR_MOLECULE));
    }

    public static Item createCharacter(int x, int y, char c) {
        int index = switch (Character.toUpperCase(c)) {
            case 'D' -> 118;
            case 'U' -> 119;
            case 'K' -> 120;
            case 'E' -> 121;
            default -> throw new IllegalArgumentException("Unsupported char: " + c);
        };

        return new Item(x, y, new SimpleSpriteRenderable(createItemSpriteDescriptor(index)), new CharacterBehavior(c));
    }

    public static Item createEquipment(int x, int y, Inventory.Equipment equipment) {
        int index = switch (equipment) { // TODO duplicated in Hud, maybe store SpriteDescriptors somewhere central
            case BOOTS -> BOOTS_SPRITE_INDEX;
            case GRAPPLING_HOOKS -> GRAPPLING_HOOKS_SPRITE_INDEX;
            case ROBOHAND -> ROBOHAND_SPRITE_INDEX;
            case ACCESS_CARD -> ACCESS_CARD_SPRITE_INDEX;
        };

        SpriteDescriptor spriteDescriptor = createItemSpriteDescriptor(OBJECTS, index);

        return new Item(x, y, new SimpleSpriteRenderable(spriteDescriptor), new EquipmentBehavior(equipment));
    }

    public static Item createFirepowerUpgrade(int x, int y) {
        return new Item(x, y, new SimpleSpriteRenderable(createItemSpriteDescriptor(OBJECTS, 43)), new FirepowerUpgradeBehavior());
    }

    private static SpriteDescriptor createItemSpriteDescriptor(int index) {
        return createItemSpriteDescriptor(OBJECTS, index);
    }

    private static SpriteDescriptor createItemSpriteDescriptor(Function<AssetManager, List<Sprite>> selector, int index) {
        return new SpriteDescriptor(selector, index);
    }

    private static AnimationDescriptor createItemAnimationDescriptor(int index) {
        return createItemAnimationDescriptor(OBJECTS, index);
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
    private static final int NUCLEAR_MOLECULE_SPRITE_INDEX = 74;
    private static final int FLAG_SPRITE_INDEX = 97;
    private static final int RADIO_SPRITE_INDEX = 102;

    private static final int SODA_SPRITE_INDEX = 132;
    private static final int FIZZING_SODA_SPRITE_INDEX = 136;

    private static final int BOOTS_SPRITE_INDEX = 10;
    private static final int GRAPPLING_HOOKS_SPRITE_INDEX = 18;
    private static final int ROBOHAND_SPRITE_INDEX = 63;
    private static final int ACCESS_CARD_SPRITE_INDEX = 64;
}

package duke.level.processors;

import duke.gameplay.Active;
import duke.gameplay.active.Box;
import duke.gameplay.active.Dynamite;
import duke.gameplay.active.items.Balloon;
import duke.gameplay.active.items.ItemFactory;
import duke.gameplay.player.Inventory;
import duke.gfx.SpriteDescriptor;
import duke.level.Level;
import duke.level.LevelBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static duke.gfx.SpriteDescriptor.OBJECTS;

public class BoxProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return BOX_DESCRIPTORS.containsKey(tileId);
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        int x = Level.toX(index);
        int y = Level.toY(index);

        BoxDescriptor descriptor = BOX_DESCRIPTORS.get(tileId);
        builder.add(new Box(x, y, descriptor.spriteDescriptor(), descriptor.contents())).replaceTile(index, LevelBuilder.LEFT);
    }

    private static final SpriteDescriptor GREY = new SpriteDescriptor(OBJECTS, 0);
    private static final SpriteDescriptor RED = new SpriteDescriptor(OBJECTS, 101);
    private static final SpriteDescriptor BLUE = new SpriteDescriptor(OBJECTS, 100);
    static final Map<Integer, BoxDescriptor> BOX_DESCRIPTORS = new HashMap<>();

    static {
        BOX_DESCRIPTORS.put(0x3000, new BoxDescriptor(GREY, null));
        BOX_DESCRIPTORS.put(0x3006, new BoxDescriptor(GREY, (x, y) -> ItemFactory.createEquipment(x, y, Inventory.Equipment.BOOTS)));
        BOX_DESCRIPTORS.put(0x3008, new BoxDescriptor(GREY, (x, y) -> ItemFactory.createEquipment(x, y, Inventory.Equipment.GRAPPLING_HOOKS)));
        BOX_DESCRIPTORS.put(0x300f, new BoxDescriptor(GREY, null)); // gun upgrade
        BOX_DESCRIPTORS.put(0x3012, new BoxDescriptor(GREY, Dynamite::new));
        BOX_DESCRIPTORS.put(0x3015, new BoxDescriptor(RED, ItemFactory::createSoda));
        BOX_DESCRIPTORS.put(0x3018, new BoxDescriptor(RED, ItemFactory::createTurkeyLeg));
        BOX_DESCRIPTORS.put(0x301d, new BoxDescriptor(BLUE, ItemFactory::createFootball));
        BOX_DESCRIPTORS.put(0x301e, new BoxDescriptor(BLUE, ItemFactory::createJoystick));
        BOX_DESCRIPTORS.put(0x301f, new BoxDescriptor(BLUE, ItemFactory::createFloppy));
        BOX_DESCRIPTORS.put(0x3020, new BoxDescriptor(GREY, (x, y) -> ItemFactory.createEquipment(x, y, Inventory.Equipment.ROBOHAND)));
        BOX_DESCRIPTORS.put(0x3023, new BoxDescriptor(BLUE, (x, y) -> new Balloon(x, y - Level.TILE_SIZE)));
        BOX_DESCRIPTORS.put(0x3029, new BoxDescriptor(GREY, ItemFactory::createNuclearMolecule));
        BOX_DESCRIPTORS.put(0x302d, new BoxDescriptor(BLUE, ItemFactory::createFlag));
        BOX_DESCRIPTORS.put(0x302e, new BoxDescriptor(BLUE, ItemFactory::createRadio));
        BOX_DESCRIPTORS.put(0x3033, new BoxDescriptor(GREY, (x, y) -> ItemFactory.createEquipment(x, y, Inventory.Equipment.ACCESS_CARD)));
        BOX_DESCRIPTORS.put(0x3037, new BoxDescriptor(GREY, (x, y) -> ItemFactory.createCharacter(x, y, 'D')));
        BOX_DESCRIPTORS.put(0x3038, new BoxDescriptor(GREY, (x, y) -> ItemFactory.createCharacter(x, y, 'U')));
        BOX_DESCRIPTORS.put(0x3039, new BoxDescriptor(GREY, (x, y) -> ItemFactory.createCharacter(x, y, 'K')));
        BOX_DESCRIPTORS.put(0x303a, new BoxDescriptor(GREY, (x, y) -> ItemFactory.createCharacter(x, y, 'E')));
    }

    private record BoxDescriptor(SpriteDescriptor spriteDescriptor,
                                 BiFunction<Integer, Integer, ? extends Active> contents) {
    }
}

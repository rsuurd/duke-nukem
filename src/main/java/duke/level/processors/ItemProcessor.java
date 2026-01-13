package duke.level.processors;

import duke.gameplay.active.items.Item;
import duke.gameplay.active.items.ItemFactory;
import duke.gameplay.active.items.Key;
import duke.level.Level;
import duke.level.LevelBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ItemProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return ITEM_MAPPINGS.containsKey(tileId);
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        Item item = ITEM_MAPPINGS.get(tileId).apply(Level.toX(index), Level.toY(index));

        builder.add(item).replaceTile(index, LevelBuilder.LEFT);
    }

    static final Map<Integer, BiFunction<Integer, Integer, Item>> ITEM_MAPPINGS = new HashMap<>();

    static {
        ITEM_MAPPINGS.put(0x3044, (x, y) -> ItemFactory.createKey(x, y, Key.Type.RED));
        ITEM_MAPPINGS.put(0x3045, (x, y) -> ItemFactory.createKey(x, y, Key.Type.GREEN));
        ITEM_MAPPINGS.put(0x3046, (x, y) -> ItemFactory.createKey(x, y, Key.Type.BLUE));
        ITEM_MAPPINGS.put(0x3047, (x, y) -> ItemFactory.createKey(x, y, Key.Type.MAGENTA));
        ITEM_MAPPINGS.put(0x3050, ItemFactory::createFootball);
        ITEM_MAPPINGS.put(0x3051, ItemFactory::createTurkeyLeg);
        ITEM_MAPPINGS.put(0x3052, ItemFactory::createSoda);
        ITEM_MAPPINGS.put(0x3053, ItemFactory::createFloppy);
        ITEM_MAPPINGS.put(0x3054, ItemFactory::createJoystick);
        ITEM_MAPPINGS.put(0x3055, ItemFactory::createFlag);
        ITEM_MAPPINGS.put(0x3056, ItemFactory::createRadio);
    }
}

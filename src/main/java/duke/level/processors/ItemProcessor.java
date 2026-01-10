package duke.level.processors;

import duke.gameplay.active.items.Item;
import duke.gameplay.active.items.ItemFactory;
import duke.level.Level;
import duke.level.LevelBuilder;

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

    static final Map<Integer, BiFunction<Integer, Integer, Item>> ITEM_MAPPINGS = Map.of(
            0x3050, ItemFactory::createFootball,
//            0x3051, ItemFactory::createTurkey,
//            0x3052, ItemFactory::createSoda,
            0x3053, ItemFactory::createFloppy,
            0x3054, ItemFactory::createJoystick,
            0x3055, ItemFactory::createFlag,
            0x3056, ItemFactory::createRadio
    );
}

package duke.level.processors;

import duke.gameplay.active.Box;
import duke.gameplay.active.items.Item;
import duke.gameplay.active.items.ItemFactory;
import duke.level.Level;
import duke.level.LevelBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

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
        builder.add(new Box(x, y, descriptor.type(), descriptor.contents())).replaceTile(index, LevelBuilder.LEFT);
    }

    static final Map<Integer, BoxDescriptor> BOX_DESCRIPTORS = new HashMap<>();

    static {
        BOX_DESCRIPTORS.put(0x3000, new BoxDescriptor(Box.Type.GREY, null));
        BOX_DESCRIPTORS.put(0x3006, new BoxDescriptor(Box.Type.GREY, null)); // shoes
        BOX_DESCRIPTORS.put(0x3008, new BoxDescriptor(Box.Type.GREY, null)); // grappling hook
        BOX_DESCRIPTORS.put(0x300f, new BoxDescriptor(Box.Type.GREY, null)); // gun upgrade
        BOX_DESCRIPTORS.put(0x3012, new BoxDescriptor(Box.Type.RED, null)); // dynamite
        BOX_DESCRIPTORS.put(0x3015, new BoxDescriptor(Box.Type.RED, null)); // soda
        BOX_DESCRIPTORS.put(0x3018, new BoxDescriptor(Box.Type.RED, null)); // turkey
        BOX_DESCRIPTORS.put(0x301d, new BoxDescriptor(Box.Type.BLUE, ItemFactory::createFootball));
        BOX_DESCRIPTORS.put(0x301e, new BoxDescriptor(Box.Type.BLUE, ItemFactory::createJoystick));
        BOX_DESCRIPTORS.put(0x301f, new BoxDescriptor(Box.Type.BLUE, ItemFactory::createFloppy));
        BOX_DESCRIPTORS.put(0x3020, new BoxDescriptor(Box.Type.GREY, null)); // robohand
        BOX_DESCRIPTORS.put(0x3023, new BoxDescriptor(Box.Type.BLUE, null)); // balloon
        BOX_DESCRIPTORS.put(0x3029, new BoxDescriptor(Box.Type.GREY, null)); // nuclear molecule
        BOX_DESCRIPTORS.put(0x302d, new BoxDescriptor(Box.Type.BLUE, ItemFactory::createFlag));
        BOX_DESCRIPTORS.put(0x302e, new BoxDescriptor(Box.Type.BLUE, ItemFactory::createRadio));
        BOX_DESCRIPTORS.put(0x3033, new BoxDescriptor(Box.Type.GREY, null)); // robohand
        BOX_DESCRIPTORS.put(0x3037, new BoxDescriptor(Box.Type.GREY, null)); // D
        BOX_DESCRIPTORS.put(0x3038, new BoxDescriptor(Box.Type.GREY, null)); // U
        BOX_DESCRIPTORS.put(0x3039, new BoxDescriptor(Box.Type.GREY, null)); // K
        BOX_DESCRIPTORS.put(0x303a, new BoxDescriptor(Box.Type.GREY, null)); // E
    }

    private record BoxDescriptor(Box.Type type, BiFunction<Integer, Integer, Item> contents) {
    }
}

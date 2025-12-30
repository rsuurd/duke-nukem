package duke.level.processors;

import duke.gameplay.active.Decoration;
import duke.gfx.SpriteDescriptor;
import duke.level.Level;
import duke.level.LevelBuilder;
import duke.resources.AssetManager;

import java.util.HashMap;
import java.util.Map;

public class DecorationProcessor implements ActiveProcessor {
    @Override
    public boolean canProcess(int tileId) {
        return MAPPINGS.containsKey(tileId);
    }

    @Override
    public void process(int index, int tileId, LevelBuilder builder) {
        builder.add(new Decoration(Level.toX(index), Level.toY(index), MAPPINGS.get(tileId)));
    }

    private static Map<Integer, SpriteDescriptor> MAPPINGS = new HashMap<>();

    static {
        SpriteDescriptor BASE_DESCRIPTOR = new SpriteDescriptor(AssetManager::getAnim, 0, 0, 0, 1, 1);

        MAPPINGS.put(0x3025, BASE_DESCRIPTOR.withBaseIndex(211));
        MAPPINGS.put(0x3026, BASE_DESCRIPTOR.withBaseIndex(212));
        MAPPINGS.put(0x3027, BASE_DESCRIPTOR.withBaseIndex(213));
        MAPPINGS.put(0x3028, BASE_DESCRIPTOR.withBaseIndex(214));
        MAPPINGS.put(0x303d, BASE_DESCRIPTOR.withBaseIndex(262));
        MAPPINGS.put(0x303e, BASE_DESCRIPTOR.withBaseIndex(263));
        MAPPINGS.put(0x303f, BASE_DESCRIPTOR.withBaseIndex(264));
    }
}

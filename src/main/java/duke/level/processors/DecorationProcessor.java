package duke.level.processors;

import duke.gameplay.Decoration;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
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
        builder.add(new Decoration(Level.toX(index), Level.toY(index), new Animation(MAPPINGS.get(tileId))));
    }

    private static Map<Integer, AnimationDescriptor> MAPPINGS = new HashMap<>();

    static {
        SpriteDescriptor BASE_DESCRIPTOR = new SpriteDescriptor(AssetManager::getAnim, 0, 0, 0, 1, 1);

        MAPPINGS.put(0x3025, new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(211), 1, 1));
        MAPPINGS.put(0x3026, new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(212), 1, 1));
        MAPPINGS.put(0x3027, new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(213), 1, 1));
        MAPPINGS.put(0x3028, new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(214), 1, 1));
        MAPPINGS.put(0x303d, new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(262), 1, 1));
        MAPPINGS.put(0x303e, new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(263), 1, 1));
        MAPPINGS.put(0x303f, new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(264), 1, 1));
    }
}

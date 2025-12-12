package duke.level.processors;

import java.util.HashMap;
import java.util.Map;

public class ActiveProcessorRegistry {
    private static final ActiveProcessor NOOP = (index, tileId, builder) -> {
    };

    private Map<Integer, ActiveProcessor> processors;

    public ActiveProcessorRegistry() {
        processors = new HashMap<>();
    }

    public void addProcessor(int tileId, ActiveProcessor processor) {
        processors.put(tileId, processor);
    }

    public ActiveProcessor getProcessor(int tileId) {
        return processors.getOrDefault(tileId, NOOP);
    }

    public static ActiveProcessorRegistry createDefault() {
        ActiveProcessorRegistry registry = new ActiveProcessorRegistry();

        registry.addProcessor(PlayerStartProcessor.TILE_ID, new PlayerStartProcessor());

        return registry;
    }
}

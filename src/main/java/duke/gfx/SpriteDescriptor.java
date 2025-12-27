package duke.gfx;

import duke.resources.AssetManager;

import java.util.List;
import java.util.function.Function;

public record SpriteDescriptor(Function<AssetManager, List<Sprite>> sheetSelector, int baseIndex,
                               int offsetX, int offsetY, int rows, int cols) {
    public SpriteDescriptor {
        if (rows <= 0) {
            throw new IllegalArgumentException("rows must be positive");
        }

        if (cols <= 0) {
            throw new IllegalArgumentException("cols must be positive");
        }
    }
}

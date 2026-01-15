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

    public SpriteDescriptor(Function<AssetManager, List<Sprite>> sheetSelector, int baseIndex) {
        this(sheetSelector, baseIndex, 0, 0, 1, 1);
    }

    public SpriteDescriptor withBaseIndex(int baseIndex) {
        return new SpriteDescriptor(sheetSelector(), baseIndex, offsetX(), offsetY(), rows(), cols());
    }

    public static final Function<AssetManager, List<Sprite>> TILES = AssetManager::getTiles;
    public static final Function<AssetManager, List<Sprite>> MAN = AssetManager::getMan;
    public static final Function<AssetManager, List<Sprite>> FONT = AssetManager::getFont;
    public static final Function<AssetManager, List<Sprite>> ANIM = AssetManager::getAnim;
    public static final Function<AssetManager, List<Sprite>> OBJECTS = AssetManager::getObjects;
    public static final Function<AssetManager, List<Sprite>> BORDER = AssetManager::getBorder;
    public static final Function<AssetManager, List<Sprite>> NUMBERS = AssetManager::getNumbers;
}

package duke.level;

import duke.DukeNukemException;
import duke.resources.AssetManager;

import java.util.Iterator;
import java.util.List;

public class LevelManager {
    private AssetManager assets;
    private Iterator<LevelDescriptor> levelIterator;

    public LevelManager(AssetManager assetManager) {
        this(assetManager, SHRAPNEL_CITY);
    }

    LevelManager(AssetManager assetManager, List<LevelDescriptor> levels) {
        this.assets = assetManager;
        levelIterator = levels.iterator();
    }

    public Level getNextLevel() {
        if (!levelIterator.hasNext()) throw new DukeNukemException("No more levels to beat");

        return assets.getLevel(levelIterator.next());
    }

    // TODO move to properties file later
    private static final String SECRET_TIP = "Secret tip: ";
    private static final String PRESS_ENTER = "      Press ENTER:";
    public static final String NOTES_HINT = SECRET_TIP + "Press the UP\nARROW to read the notes\n\n" + PRESS_ENTER;

    private static final List<LevelDescriptor> SHRAPNEL_CITY = List.of(
            new LevelDescriptor(1, 0),
            new LevelDescriptor(2, 0, NOTES_HINT),
            new LevelDescriptor(3, 0)
    );
}

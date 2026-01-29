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
    private static final List<LevelDescriptor> SHRAPNEL_CITY = List.of(
            new LevelDescriptor(1, 0),
            new LevelDescriptor(2, 0, "You are entering the\nnext level. Now is a\ngood time to SAVE your\ngame."),
            new LevelDescriptor(3, 0, "Surprise Duke, I'll be\ntracking your every move\nwith my security camera\nsystem!")
    );
}

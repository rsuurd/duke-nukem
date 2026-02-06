package duke.level;

import duke.DukeNukemException;
import duke.resources.AssetManager;

import java.util.Iterator;
import java.util.List;

public class LevelManager {
    private AssetManager assets;
    private List<LevelDescriptor> levels;
    private Iterator<LevelDescriptor> levelIterator;

    public LevelManager(AssetManager assetManager) {
        this(assetManager, SHRAPNEL_CITY);
    }

    LevelManager(AssetManager assetManager, List<LevelDescriptor> levels) {
        this.assets = assetManager;
        this.levels = levels;

        levelIterator = levels.iterator();
    }

    public Level getNextLevel() {
        if (!levelIterator.hasNext()) throw new DukeNukemException("No more levels to beat");

        return assets.getLevel(levelIterator.next());
    }

    public Level warpTo(int number) {
        levelIterator = levels.stream().skip(number - 1).iterator();

        return assets.getLevel(levelIterator.next());
    }

    // TODO move to properties file or EpisodeDescriptor later
    private static final List<LevelDescriptor> SHRAPNEL_CITY = List.of(
            new LevelDescriptor(1, 0),
            new LevelDescriptor(2, 0, "You are entering the\nnext level. Now is a\ngood time to SAVE your\ngame."),
            new LevelDescriptor(3, 0, "Surprise Duke, I'll be\ntracking your every move\nwith my security camera\nsystem!"),
            new LevelDescriptor(2, 0, "On the next level you'll\nneed to find a special\naccess card that looks\nlike a computer board."),
            new LevelDescriptor(4, 7),
            new LevelDescriptor(2, 0, "If you find the boots on\nthe next level you'll be\nable to jump higher!\nSAVE your game now."),
            new LevelDescriptor(5, 5, "I see you're still\ntrying Duke. Never mind,\nmy robot drones will\nsoon be the end of you!"),
            new LevelDescriptor(2, 0, "The next level is very\ntough--but there's also\na secret shortcut!\nDon't forget to SAVE."),
            new LevelDescriptor(6, 0, "Hmmm... I see you've\nsurvived my Maze of\nMadness. I might have\nunderestimated you."),
            new LevelDescriptor(2, 0, "You are doing quite well\nso far...Let's see if\nyou can keep it up. Also\nremember to SAVE.")
    );
}

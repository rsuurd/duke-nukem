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
            new LevelDescriptor(2, 0, "You are entering the\nnext level. Now is a\ngood time to SAVE your\ngame.\n"),
            new LevelDescriptor(3, 0, "Surprise Duke, I'll be\ntracking your every move\nwith my security camera\nsystem!\n"),
            new LevelDescriptor(2, 0, "On the next level you'll\nneed to find a special\naccess card that looks\nlike a computer board.\n"),
            new LevelDescriptor(4, 7),
            new LevelDescriptor(2, 0, "If you find the boots on\nthe next level you'll be\nable to jump higher!\nSAVE your game now.\n"),
            new LevelDescriptor(5, 3, 5, "I see you're still\ntrying Duke. Never mind,\nmy robot drones will\nsoon be the end of you!\n"),
            new LevelDescriptor(2, 0, "The next level is very\ntough--but there's also\na secret shortcut!\nDon't forget to SAVE.\n"),
            new LevelDescriptor(6, 0, "Hmmm... I see you've\nsurvived my Maze of\nMadness. I might have\nunderestimated you.\n"),
            new LevelDescriptor(2, 0, "You are doing quite well\nso far...Let's see if\nyou can keep it up. Also\nremember to SAVE.\n"),
            new LevelDescriptor(7, 2, "Duke, in spite of your\npersistance, my plan to\nrule earth will not be\nstopped. Ah, ha, ha!\n"), // sic
            new LevelDescriptor(2, 0, "Find the GRAPPLING HOOKS\nto beat the next level.\nUse them to grab on to\nspecial grated ceilings.\n"),
            new LevelDescriptor(8, 1, "Why bother Duke? Not\neven Commander Keen\ncould survive the perils\nof my mercury mines!\n"),
            new LevelDescriptor(2, 0, "There are several secret\nbonuses you can earn on\neach level! Register to\nget the complete list.\n"),
            new LevelDescriptor(9, 3, "I hate heroes! Your luck\nis about to run out.\nPerhaps now would be a\ngood time to flee, Duke!\n"),
            new LevelDescriptor(2, 0, "Secret Bonus Hint:\nCollect the letters\nD, U, K, E in order to\nearn bonus number five.\n"),
            new LevelDescriptor(10, 5, "Duke, join me now and\nlive, or continue this\nfoolishness and I'll\nsquash you myself!\n"),
            new LevelDescriptor(2, 0, "You are about to enter\nthe secret Bubble City\nfortress of Dr. Proton.\nThere's no turning back!\n"),
            new LevelDescriptor(11, 0, 11, "I'm coming down to crush\nyou, Duke. It's just\nyou and me now.\n")
    );
}

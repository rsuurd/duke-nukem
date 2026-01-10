package duke.gameplay;

import duke.level.Level;
import duke.sfx.SoundManager;

public class GameplayContext {
    private Player player;
    private Level level;
    private BoltManager boltManager;
    private ActiveManager activeManager;
    private SoundManager soundManager;
    private ScoreManager scoreManager;

    public GameplayContext(Player player, Level level, BoltManager boltManager, ActiveManager activeManager, SoundManager soundManager, ScoreManager scoreManager) {
        this.player = player;
        this.level = level;
        this.boltManager = boltManager;
        this.activeManager = activeManager;
        this.soundManager = soundManager;
        this.scoreManager = scoreManager;
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public BoltManager getBoltManager() {
        return boltManager;
    }

    public ActiveManager getActiveManager() {
        return activeManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }
}

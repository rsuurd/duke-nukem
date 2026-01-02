package duke.gameplay;

import duke.level.Level;
import duke.sfx.SoundManager;

public class GameplayContext {
    private Player player;
    private Level level;
    private ActiveManager activeManager;
    private SoundManager soundManager;

    public GameplayContext(Player player, Level level, ActiveManager activeManager, SoundManager soundManager) {
        this.player = player;
        this.level = level;
        this.activeManager = activeManager;
        this.soundManager = soundManager;
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public ActiveManager getActiveManager() {
        return activeManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
}

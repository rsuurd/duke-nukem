package duke.gameplay;

import duke.level.Level;

public class GameplayContext {
    private Player player;
    private Level level;
    private ActiveManager activeManager;

    public GameplayContext(Player player, Level level, ActiveManager activeManager) {
        this.player = player;
        this.level = level;
        this.activeManager = activeManager;
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
}

package duke.gameplay;

import duke.gameplay.player.Player;
import duke.level.Level;
import duke.sfx.SoundManager;

public class GameplayContext implements WorldQuery {
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

    @Override
    public boolean isSolid(int row, int col) {
        return level.isSolid(row, col) || occupiedBySolid(row, col);
    }

    private boolean occupiedBySolid(int row, int col) {
        int x = col * Level.TILE_SIZE;
        int y = row * Level.TILE_SIZE;

        for (Active active : activeManager.getActives()) {
            if (active instanceof Solid solid && solid.isSolid() && active.intersects(x, y, Level.TILE_SIZE, Level.TILE_SIZE)) {
                return true;
            }
        }

        return false;
    }
}

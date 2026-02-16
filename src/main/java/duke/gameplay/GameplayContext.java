package duke.gameplay;

import duke.dialog.DialogManager;
import duke.dialog.Hints;
import duke.gameplay.player.Player;
import duke.gfx.Viewport;
import duke.level.Flags;
import duke.level.Level;
import duke.sfx.SoundManager;

public class GameplayContext implements WorldQuery {
    private Player player;
    private Level level;

    private BoltManager boltManager;
    private ActiveManager activeManager;
    private SoundManager soundManager;
    private ScoreManager scoreManager;
    private BonusTracker bonusTracker;
    private DialogManager dialogManager;
    private Hints hints;
    private ViewportManager viewportManager;

    public GameplayContext(Player player, Level level, BoltManager boltManager, ActiveManager activeManager, SoundManager soundManager,
                           ScoreManager scoreManager, BonusTracker bonusTracker, DialogManager dialogManager, Hints hints, ViewportManager viewportManager) {
        this.player = player;
        this.level = level;

        this.boltManager = boltManager;
        this.activeManager = activeManager;
        this.soundManager = soundManager;
        this.scoreManager = scoreManager;
        this.bonusTracker = bonusTracker;
        this.dialogManager = dialogManager;
        this.hints = hints;
        this.viewportManager = viewportManager;
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

    public BonusTracker getBonusTracker() {
        return bonusTracker;
    }

    public DialogManager getDialogManager() {
        return dialogManager;
    }

    public Hints getHints() {
        return hints;
    }

    public ViewportManager getViewportManager() {
        return viewportManager;
    }

    @Override
    public boolean isSolid(int row, int col) {
        return Flags.SOLID.isSet(getTileFlags(row, col)) || occupiedBySolid(row, col);
    }

    @Override
    public int getTileFlags(int row, int col) {
        return level.getTileFlags(row, col);
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

    public void switchLevel(Level level) {
        this.level = level;

        activeManager.reset();
        boltManager.reset();

        level.getActives().forEach(active -> activeManager.spawn(active));

        player.setX(level.getPlayerStartX());
        player.setY(level.getPlayerStartY());
        player.enableControls();
        player.getHealth().grantInvulnerability();

        if (level.getDescriptor().isHallway()) {
            bonusTracker.reward(this);
        } else {
            bonusTracker.reset(level);
        }
    }
}

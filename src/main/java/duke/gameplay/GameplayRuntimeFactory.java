package duke.gameplay;

import duke.GameSystems;
import duke.dialog.Hints;
import duke.gameplay.player.Player;
import duke.gfx.SpriteRenderer;
import duke.gfx.Viewport;
import duke.level.LevelManager;
import duke.resources.AssetManager;

public class GameplayRuntimeFactory {
    public GameplayRuntime createRuntime(GameSystems systems) {
        AssetManager assets = systems.getAssets();

        LevelManager levelManager = new LevelManager(assets);

        Viewport viewport = new Viewport();
        SpriteRenderer spriteRenderer = new SpriteRenderer(assets);
        Collision collision = new Collision();

        BoltManager boltManager = new BoltManager(viewport, spriteRenderer);
        ActiveManager activeManager = new ActiveManager(viewport, collision, spriteRenderer);
        ScoreManager scoreManager = new ScoreManager(activeManager);
        BonusTracker bonusTracker = new BonusTracker();
        Player player = new Player();
        ViewportManager viewportManager = new ViewportManager(player, true);

        GameplayContext context = new GameplayContext(player, null, boltManager, activeManager, systems.getSoundManager(), scoreManager, bonusTracker, systems.getDialogManager(), new Hints(), viewportManager);

        return new GameplayRuntime(levelManager, viewport, collision, context);
    }

    public record GameplayRuntime(
            LevelManager levelManager,
            Viewport viewport,
            Collision collision,
            GameplayContext context
    ) {
    }
}

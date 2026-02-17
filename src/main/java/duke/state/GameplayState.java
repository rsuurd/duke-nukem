package duke.state;

import duke.GameSystems;
import duke.Renderer;
import duke.dialog.Hints;
import duke.gameplay.*;
import duke.gameplay.player.Player;
import duke.gfx.*;
import duke.level.Level;
import duke.level.LevelManager;
import duke.resources.AssetManager;
import duke.ui.KeyHandler;

// TODO refactor for testing
public class GameplayState implements GameState {
    private LevelManager levelManager;
    private LevelRenderer levelRenderer;
    private Viewport viewport;
    private Hud hud;
    private SpriteRenderer spriteRenderer;
    private Collision collision;
    private GameplayContext context;
    private Cheats cheats;

    // TODO fix construction
    public GameplayState(GameSystems systems, Cheats cheats) {
        AssetManager assets = systems.getAssets();

        levelManager = new LevelManager(assets);
        levelRenderer = new LevelRenderer(assets, null);

        viewport = new Viewport();
        hud = new Hud(assets, new Font(assets));
        spriteRenderer = new SpriteRenderer(assets);
        collision = new Collision();

        context = createGameplayContext(systems);

        this.cheats = cheats;
    }

    private GameplayContext createGameplayContext(GameSystems systems) {
        BoltManager boltManager = new BoltManager(viewport, spriteRenderer);
        ActiveManager activeManager = new ActiveManager(viewport, collision, spriteRenderer);
        ScoreManager scoreManager = new ScoreManager(activeManager);
        BonusTracker bonusTracker = new BonusTracker();
        Player player = new Player();
        ViewportManager viewportManager = new ViewportManager(player, true);

        return new GameplayContext(player, null, boltManager, activeManager, systems.getSoundManager(), scoreManager, bonusTracker, systems.getDialogManager(), new Hints(), viewportManager);
    }

    GameplayState(LevelManager levelManager, LevelRenderer levelRenderer, Viewport viewport, Hud hud, SpriteRenderer spriteRenderer, Collision collision, GameplayContext context, Cheats cheats) {
        this.levelManager = levelManager;
        this.levelRenderer = levelRenderer;
        this.viewport = viewport;
        this.hud = hud;
        this.spriteRenderer = spriteRenderer;
        this.collision = collision;
        this.context = context;
        this.cheats = cheats;
    }

    @Override
    public void start(GameSystems systems) {
//        Level level = levelManager.warpTo(19);
        Level level = levelManager.getNextLevel();
        switchLevel(level, context);
    }

    private void switchLevel(Level level, GameplayContext context) {
        context.switchLevel(level);
        levelRenderer.setLevel(level);
        viewport.center(context.getViewportManager().getTarget());
    }

    @Override
    public void update(GameSystems systems) {
        if (systems.getDialogManager().hasDialog()) {
            systems.getDialogManager().update(systems);
            return;
        }

        updatePlayer(systems.getKeyHandler().getInput());

        if (context.getViewportManager().pollSnapToCenter()) {
            viewport.center(context.getViewportManager().getTarget());
        } else {
            viewport.update(context.getViewportManager().getTarget());
        }

        context.getBoltManager().update(context);
        context.getActiveManager().update(context);

        if (context.getLevel().isCompleted()) {
            switchLevel(levelManager.getNextLevel(), context);
        }

        cheats.processInput(systems.getKeyHandler(), context);
    }

    private void updatePlayer(KeyHandler.Input input) {
        Player player = context.getPlayer();

        player.processInput(input);
        player.update(context);
        collision.resolve(player, context);
        player.postMovement(context);
    }

    @Override
    public void render(GameSystems systems) {
        Renderer renderer = systems.getRenderer();
        levelRenderer.render(renderer, viewport);
        context.getActiveManager().render(renderer, Layer.BACKGROUND);
        drawPlayer(renderer);
        context.getActiveManager().render(renderer, Layer.FOREGROUND);
        context.getBoltManager().render(renderer);
        context.getActiveManager().render(renderer, Layer.POST_PROCESS);

        hud.render(renderer, context.getScoreManager().getScore(), context.getPlayer(), getDebugString(context));
        context.getDialogManager().render(renderer);
    }

    private String getDebugString(GameplayContext context) {
        Player player = context.getPlayer();
        return """
                position: %d, %d
                velocity: %d, %d
                %s %s
                """.formatted(player.getX(), player.getY(), player.getVelocityX(), player.getVelocityY(), player.getState(), player.getFacing());
    }

    private void drawPlayer(Renderer renderer) {
        Player player = context.getPlayer();

        int x = viewport.toScreenX(player.getX());
        int y = viewport.toScreenY(player.getY());

        spriteRenderer.render(renderer, player, x, y);
    }
}

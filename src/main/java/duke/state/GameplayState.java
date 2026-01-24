package duke.state;

import duke.GameContext;
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

    // TODO fix construction
    public GameplayState(GameContext gameContext) {
        AssetManager assets = gameContext.getAssets();

        levelManager = new LevelManager(assets);
        levelRenderer = new LevelRenderer(assets, null);

        viewport = new Viewport();
        hud = new Hud(assets, new Font(assets));
        spriteRenderer = new SpriteRenderer(assets);
        collision = new Collision();

        context = createGameplayContext(gameContext);
    }

    private GameplayContext createGameplayContext(GameContext gameContext) {
        BoltManager boltManager = new BoltManager(viewport, spriteRenderer);
        ActiveManager activeManager = new ActiveManager(viewport, collision, spriteRenderer);
        ScoreManager scoreManager = new ScoreManager(activeManager);
        BonusTracker bonusTracker = new BonusTracker();

        return new GameplayContext(new Player(), null, boltManager, activeManager, gameContext.getSoundManager(), scoreManager, bonusTracker, gameContext.getDialogManager(), new Hints());
    }

    GameplayState(LevelManager levelManager, LevelRenderer levelRenderer, Viewport viewport, Hud hud, SpriteRenderer spriteRenderer, Collision collision, GameplayContext context) {
        this.levelManager = levelManager;
        this.levelRenderer = levelRenderer;
        this.viewport = viewport;
        this.hud = hud;
        this.spriteRenderer = spriteRenderer;
        this.collision = collision;
        this.context = context;
    }

    @Override
    public void start(GameContext gameContext) {
//        levelManager.getNextLevel();
        switchLevel(levelManager.getNextLevel(), context);
    }

    private void switchLevel(Level level, GameplayContext context) {
        context.switchLevel(level);
        levelRenderer.setLevel(level);
        viewport.center(context.getPlayer().getX(), context.getPlayer().getY());
    }

    @Override
    public void update(GameContext gameContext) {
        if (gameContext.getDialogManager().hasDialog()) {
            gameContext.getDialogManager().update(gameContext);
            return;
        }

        updatePlayer(gameContext.getKeyHandler().getInput());
        context.getBoltManager().update(context);
        context.getActiveManager().update(context);

        if (context.getLevel().isCompleted()) {
            switchLevel(levelManager.getNextLevel(), context);
        }
    }

    private void updatePlayer(KeyHandler.Input input) {
        Player player = context.getPlayer();

        player.processInput(input);
        player.update(context);
        collision.resolve(player, context);
        player.postMovement(context);

        viewport.update(player.getX(), player.getY(), player.isGrounded());
    }

    @Override
    public void render(GameContext gameContext) {
        Renderer renderer = gameContext.getRenderer();
        levelRenderer.render(renderer, viewport);
        context.getActiveManager().render(renderer, Layer.BACKGROUND);
        drawPlayer(renderer);
        context.getActiveManager().render(renderer, Layer.FOREGROUND);
        context.getBoltManager().render(renderer);
        hud.render(renderer, context.getScoreManager().getScore(), context.getPlayer());
        context.getDialogManager().render(renderer);
    }

    private void drawPlayer(Renderer renderer) {
        Player player = context.getPlayer();

        int x = viewport.toScreenX(player.getX());
        int y = viewport.toScreenY(player.getY());

        spriteRenderer.render(renderer, player, x, y);
    }
}

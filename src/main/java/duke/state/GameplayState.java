package duke.state;

import duke.GameSystems;
import duke.Renderer;
import duke.dialog.Dialog;
import duke.dialog.Hints;
import duke.gameplay.*;
import duke.gameplay.player.Player;
import duke.gfx.*;
import duke.level.Level;
import duke.level.LevelManager;
import duke.menu.HelpMenu;
import duke.menu.MenuManager;
import duke.resources.AssetManager;
import duke.ui.KeyHandler;

import static duke.level.Level.TILE_SIZE;
import static java.awt.event.KeyEvent.*;

// TODO refactor for testing, should have just a noargs constructor and construct everything on start like the other states
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
        checkInput(systems);

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

    }

    private void checkInput(GameSystems systems) {
        updateHelpMenu(systems);
        checkSoundToggle(systems);
        checkHintsToggle(systems);
        // speed buttons < >
        cheats.processInput(systems.getKeyHandler(), context);
    }

    private void updateHelpMenu(GameSystems systems) {
        MenuManager menuManager = systems.getMenuManager();

        if (systems.getKeyHandler().consume(VK_F1)) {
            menuManager.open(new HelpMenu(context), systems);
        }

        menuManager.update(systems);
    }

    private void checkSoundToggle(GameSystems systems) {
        if (systems.getKeyHandler().consume(VK_S)) {
            context.getSoundManager().toggle();
            String status = context.getSoundManager().isEnabled() ? "on" : "off";

            systems.getDialogManager().open(new Dialog("       Sound toggle\n\n     The sound is %s.".formatted(status), TILE_SIZE, 3 * TILE_SIZE, 3, 13, true, true));
        }
    }

    private void checkHintsToggle(GameSystems systems) {
        if (systems.getKeyHandler().consume(VK_H)) {
            context.getHints().toggle();
            String status = context.getHints().isEnabled() ? "on" : "off";

            systems.getDialogManager().open(new Dialog("       Hint toggle\n\n       Hints are %s.".formatted(status), TILE_SIZE, 3 * TILE_SIZE, 3, 13, true, true));
        }
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

package duke.state;

import duke.GameSystems;
import duke.Renderer;
import duke.dialog.Dialog;
import duke.gameplay.*;
import duke.gameplay.player.Inventory;
import duke.gameplay.player.Player;
import duke.gfx.*;
import duke.level.Level;
import duke.level.LevelManager;
import duke.menu.HelpMenu;
import duke.menu.MenuManager;
import duke.ui.KeyHandler;

import static duke.level.Level.TILE_SIZE;
import static java.awt.event.KeyEvent.*;

public class GameplayState implements GameState {
    private GameplayRuntimeFactory runtimeFactory;

    private SaveGame saveGame;

    private LevelManager levelManager;
    private Viewport viewport;
    private Collision collision;
    private GameplayContext context;

    private LevelRenderer levelRenderer;
    private SpriteRenderer spriteRenderer;
    private Hud hud;

    private Cheats cheats;

    public GameplayState() {
        this(null);
    }

    public GameplayState(SaveGame saveGame) {
        this(new GameplayRuntimeFactory(), saveGame);
    }

    public GameplayState(GameplayRuntimeFactory factory, SaveGame saveGame) {
        this.runtimeFactory = factory;

        this.saveGame = saveGame;
    }

    @Override
    public void start(GameSystems systems) {
        initializeRuntime(systems);

        if (saveGame == null) {
            startNewGame();
        } else {
            resumeGame();
        }
    }

    private void initializeRuntime(GameSystems systems) {
        GameplayRuntimeFactory.GameplayRuntime runtime = runtimeFactory.createRuntime(systems);

        levelManager = runtime.levelManager();
        viewport = runtime.viewport();
        collision = runtime.collision();
        context = runtime.context();

        levelRenderer = new LevelRenderer(systems.getAssets(), null);
        spriteRenderer = new SpriteRenderer(systems.getAssets());
        hud = new Hud(systems.getAssets(), new Font(systems.getAssets()));

        cheats = new Cheats(true); // systems.getParameters().asp();
    }

    private void startNewGame() {
        switchLevel(levelManager.getNextLevel());
    }

    private void resumeGame() {
        switchLevel(levelManager.warpTo(saveGame.level()));

        // TODO calculate cam / player positions instead of just relying on playerStartLocation
        context.getPlayer().getHealth().setCurrent(saveGame.health());
        context.getPlayer().getWeapon().setFirepower(saveGame.firepower());

        // clear inventory? Right now it works because we swap between gamestates, but it might be good to be explicit.
        for (Inventory.Equipment equipment : Inventory.Equipment.values()) {
            if (saveGame.inventory().isEquippedWith(equipment)) {
                context.getPlayer().getInventory().addEquipment(equipment);
            }
        }

        context.getHints().setAvailableHints(saveGame.hints());
        context.getScoreManager().setScore(saveGame.score());
    }

    private void switchLevel(Level level) {
        context.switchLevel(level);
        levelRenderer.setLevel(level);
        viewport.center(context.getViewportManager().getTarget());

        if (level.getDescriptor().isHallway()) {
            saveGame = new SaveGameFactory().create(context);
        }
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

        checkLevelStatus(systems);
    }

    private void checkInput(GameSystems systems) {
        // check for ESC to quit

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

    private void checkLevelStatus(GameSystems systems) {
        Level level = context.getLevel();
        StateRequester stateRequester = systems.getStateRequester();

        if (level.isRestartRequested()) {
            if (!stateRequester.isTransitioning()) {
                stateRequester.requestState(new GetReady(saveGame), StateRequester.Transition.FADE_TO_WHITE);
            }
        } else if (level.isCompleted()) {
            if (levelManager.isLast()) {
                if (!stateRequester.isTransitioning()) {
                    stateRequester.requestState(new End());
                }
            } else {
                switchLevel(levelManager.getNextLevel());
            }
        }
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

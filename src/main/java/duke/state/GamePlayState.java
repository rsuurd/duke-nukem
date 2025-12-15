package duke.state;

import duke.GameContext;
import duke.Renderer;
import duke.gameplay.Collision;
import duke.gameplay.Player;
import duke.gfx.*;
import duke.level.Level;
import duke.resources.AssetManager;

import java.util.Arrays;

// TODO refactor for testing
public class GamePlayState implements GameState {
    private Level level;
    private Viewport viewport;
    private LevelRenderer levelRenderer;
    private Hud hud;

    private Player player;
    private Collision collision;

    // TODO fix construction
    public GamePlayState(AssetManager assets, Font font, Level level) {
        this(level, new Viewport(), new LevelRenderer(assets, level), new Hud(assets, font), new Player(), new Collision());
    }

    GamePlayState(Level level, Viewport viewport, LevelRenderer levelRenderer, Hud hud, Player player, Collision collision) {
        this.level = level;
        this.viewport = viewport;
        this.levelRenderer = levelRenderer;
        this.hud = hud;
        this.player = player;
        this.collision = collision;
    }

    @Override
    public void start(GameContext context) {
        player.moveTo(level.getPlayerStartX(), level.getPlayerStartY());
        viewport.center(player.getX(), player.getY());
    }

    @Override
    public void update(GameContext context) {
        player.processInput(context.getKeyHandler());
        collision.resolve(player, level);
        viewport.update(player.getX(), player.getY(), player.isGrounded());
    }

    @Override
    public void render(GameContext context) {
        Renderer renderer = context.getRenderer();

        levelRenderer.render(renderer, viewport);

        // if collides, color hitbox red
        Arrays.fill(HITBOX.getPixels(), (byte) 10);
        renderer.draw(HITBOX, viewport.toScreenX(player.getX()), viewport.toScreenY(player.getY()));

        // render enemies
        // projectiles / effects etc
        hud.render(renderer);
    }

    // temporary graphic
    private static final Sprite HITBOX = new Sprite(16, 32);
}

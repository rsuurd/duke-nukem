package duke.state;

import duke.GameContext;
import duke.Renderer;
import duke.gameplay.Active;
import duke.gameplay.Collision;
import duke.gameplay.Player;
import duke.gameplay.SpriteRenderable;
import duke.gfx.*;
import duke.level.Level;
import duke.resources.AssetManager;

// TODO refactor for testing
public class GamePlayState implements GameState {
    private AssetManager assets;
    private Level level;
    private Viewport viewport;
    private LevelRenderer levelRenderer;
    private Hud hud;
    private Font font;
    private SpriteRenderer spriteRenderer;

    private Player player;
    private Collision collision;

    // TODO fix construction
    public GamePlayState(AssetManager assets, Font font, Level level) {
        this(level, new Viewport(), new LevelRenderer(assets, level), new Hud(assets, font), font, new Player(), new Collision(), new SpriteRenderer(assets));
    }

    GamePlayState(Level level, Viewport viewport, LevelRenderer levelRenderer, Hud hud, Font font, Player player, Collision collision, SpriteRenderer spriteRenderer) {
        this.level = level;
        this.viewport = viewport;
        this.levelRenderer = levelRenderer;
        this.hud = hud;
        this.font = font;
        this.player = player;
        this.collision = collision;
        this.spriteRenderer = spriteRenderer;
    }

    @Override
    public void start(GameContext context) {
        player.setX(level.getPlayerStartX());
        player.setY(level.getPlayerStartY());
        viewport.center(player.getX(), player.getY());
    }

    @Override
    public void update(GameContext context) {
        player.processInput(context.getKeyHandler().getInput());
        player.update();
        collision.resolve(player, level);
        viewport.update(player.getX(), player.getY(), player.isGrounded());
    }

    @Override
    public void render(GameContext context) {
        Renderer renderer = context.getRenderer();

        levelRenderer.render(renderer, viewport);

        level.getActives().stream().filter(viewport::isVisible).forEach(active -> {
            if (active instanceof SpriteRenderable renderable) {
                int x = viewport.toScreenX(active.getX());
                int y = viewport.toScreenY(active.getY());

                spriteRenderer.render(renderer, renderable, x, y);
            }
        });

        // render enemies
        // projectiles / effects etc

        hud.render(renderer, 0, player.getHealth());

        drawPlayer(renderer);
        drawDebugInfo(renderer);
    }

    private void drawPlayer(Renderer renderer) {
        int x = viewport.toScreenX(player.getX());
        int y = viewport.toScreenY(player.getY());

        spriteRenderer.render(renderer, player, x, y);
    }

    private void drawDebugInfo(Renderer renderer) {
        font.drawText(renderer, String.format("position: %d, %d", player.getX(), player.getY()), 16, 16);
        font.drawText(renderer, String.format("velocity: %d, %d", player.getVelocityX(), player.getVelocityY()), 16, 24);
        font.drawText(renderer, String.format("%s %s", player.getState(), player.getFacing()), 16, 32);
    }
}

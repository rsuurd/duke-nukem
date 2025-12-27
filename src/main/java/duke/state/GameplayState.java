package duke.state;

import duke.GameContext;
import duke.Renderer;
import duke.gameplay.*;
import duke.gfx.*;
import duke.level.Level;
import duke.resources.AssetManager;

import java.util.List;

// TODO refactor for testing
public class GameplayState implements GameState {
    private AssetManager assets;

    private Viewport viewport;
    private LevelRenderer levelRenderer;
    private Hud hud;
    private Font font;
    private SpriteRenderer spriteRenderer;

    private Collision collision;

    private GameplayContext context;

    // TODO fix construction
    public GameplayState(AssetManager assets) {
        this.assets = assets;

        viewport = new Viewport();
        Level level = assets.getLevel(1);

        levelRenderer = new LevelRenderer(assets, level);
        font = new Font(assets);
        hud = new Hud(assets, font);
        spriteRenderer = new SpriteRenderer(assets);
        collision = new Collision();
        context = new GameplayContext(new Player(), level);
    }

    GameplayState(AssetManager assets, Viewport viewport, LevelRenderer levelRenderer, Hud hud, Font font, SpriteRenderer spriteRenderer, Collision collision, GameplayContext context) {
        this.assets = assets;
        this.viewport = viewport;
        this.levelRenderer = levelRenderer;
        this.hud = hud;
        this.font = font;
        this.spriteRenderer = spriteRenderer;
        this.collision = collision;
        this.context = context;
    }


    @Override
    public void start(GameContext gameContext) {
        // create new context
        Player player = context.player();
        Level level = context.level();

        player.setX(level.getPlayerStartX());
        player.setY(level.getPlayerStartY());
        viewport.center(player.getX(), player.getY());
    }

    @Override
    public void update(GameContext gameContext) {
        Player player = context.player();

        player.processInput(gameContext.getKeyHandler().getInput());
        player.update(context);
        collision.resolve(player, context.level());

        getVisibleActives().forEach(active -> {
            if (active instanceof Updatable updatable) {
                updatable.update(context);
            }
        });

        viewport.update(player.getX(), player.getY(), player.isGrounded());
    }

    @Override
    public void render(GameContext gameContext) {
        Renderer renderer = gameContext.getRenderer();

        levelRenderer.render(renderer, viewport);

        getVisibleActives().forEach(active -> {
            if (active instanceof SpriteRenderable renderable) {
                int x = viewport.toScreenX(active.getX());
                int y = viewport.toScreenY(active.getY());

                spriteRenderer.render(renderer, renderable, x, y);
            }
        });

        // render enemies
        // projectiles / effects etc

        // context.score?
        hud.render(renderer, 0, context.player().getHealth());

        drawPlayer(renderer);
        drawDebugInfo(renderer);
    }

    private List<Active> getVisibleActives() {
        // TODO grab actives from context instead if level
        // TODO allow filtering to a subclass of Active (Updatable / Renderable / etc)
        return context.level().getActives().stream().filter(viewport::isVisible).toList();
    }

    private void drawPlayer(Renderer renderer) {
        Player player = context.player();

        int x = viewport.toScreenX(player.getX());
        int y = viewport.toScreenY(player.getY());

        spriteRenderer.render(renderer, player, x, y);
    }

    private void drawDebugInfo(Renderer renderer) {
        Player player = context.player();

        font.drawText(renderer, String.format("position: %d, %d", player.getX(), player.getY()), 16, 16);
        font.drawText(renderer, String.format("velocity: %d, %d", player.getVelocityX(), player.getVelocityY()), 16, 24);
        font.drawText(renderer, String.format("%s %s", player.getState(), player.getFacing()), 16, 32);
    }
}

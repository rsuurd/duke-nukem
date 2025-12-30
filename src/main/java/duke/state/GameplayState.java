package duke.state;

import duke.GameContext;
import duke.Renderer;
import duke.gameplay.*;
import duke.gfx.*;
import duke.level.Level;
import duke.resources.AssetManager;

import java.util.Iterator;

// TODO refactor for testing
public class GameplayState implements GameState {
    private Viewport viewport;
    private LevelRenderer levelRenderer;
    private Hud hud;
    private Font font;
    private SpriteRenderer spriteRenderer;

    private Collision collision;

    private GameplayContext context;

    // TODO fix construction
    public GameplayState(AssetManager assets) {
        viewport = new Viewport();
        Level level = assets.getLevel(1);

        levelRenderer = new LevelRenderer(assets, level);
        font = new Font(assets);
        hud = new Hud(assets, font);
        spriteRenderer = new SpriteRenderer(assets);
        collision = new Collision();
        context = new GameplayContext(new Player(), level);
    }

    GameplayState(Viewport viewport, LevelRenderer levelRenderer, Hud hud, Font font, SpriteRenderer spriteRenderer, Collision collision, GameplayContext context) {
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
        Player player = context.getPlayer();
        Level level = context.getLevel();

        level.getActives().forEach(active -> context.spawn(active));

        player.setX(level.getPlayerStartX());
        player.setY(level.getPlayerStartY());
        viewport.center(player.getX(), player.getY());
    }

    @Override
    public void update(GameContext gameContext) {
        Player player = context.getPlayer();

        player.processInput(gameContext.getKeyHandler().getInput());
        player.update(context);
        collision.resolve(player, context.getLevel());

        if (player.isFiring()) {
            context.spawn(Bolt.create(player));
        }

        viewport.update(player.getX(), player.getY(), player.isGrounded());

        updateActives();
    }

    @Override
    public void render(GameContext gameContext) {
        Renderer renderer = gameContext.getRenderer();
        levelRenderer.render(renderer, viewport);

        // background
        context.getActives().forEach(active -> {
            // TODO should probably let spriterenderer do some visiblity checks
            if (viewport.isVisible(active) && active instanceof SpriteRenderable renderable) {
                int x = viewport.toScreenX(active.getX());
                int y = viewport.toScreenY(active.getY());

                spriteRenderer.render(renderer, renderable, x, y);
            }
        });

        // player
        drawPlayer(renderer);

        // foreground

        hud.render(renderer, 0, context.getPlayer().getHealth());

        drawDebugInfo(renderer);
    }

    private void updateActives() {
        Iterator<Active> iterator = context.getActives().iterator();

        while (iterator.hasNext()) {
            Active active = iterator.next();

            if (viewport.isVisible(active)) {
                if (active instanceof Updatable updatable) {
                    updatable.update(context);

                    if (active instanceof Bolt bolt && !bolt.isActive()) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    private void drawPlayer(Renderer renderer) {
        Player player = context.getPlayer();

        int x = viewport.toScreenX(player.getX());
        int y = viewport.toScreenY(player.getY());

        spriteRenderer.render(renderer, player, x, y);
    }

    private void drawDebugInfo(Renderer renderer) {
        Player player = context.getPlayer();

        font.drawText(renderer, String.format("position: %d, %d", player.getX(), player.getY()), 16, 16);
        font.drawText(renderer, String.format("velocity: %d, %d", player.getVelocityX(), player.getVelocityY()), 16, 24);
        font.drawText(renderer, String.format("%s %s", player.getState(), player.getFacing()), 16, 32);
    }
}

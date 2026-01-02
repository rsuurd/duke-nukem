package duke.state;

import duke.GameContext;
import duke.Renderer;
import duke.gameplay.*;
import duke.gfx.*;
import duke.level.Level;
import duke.resources.AssetManager;
import duke.ui.KeyHandler;

import static duke.sfx.SoundManager.SFX_BOLT_INDEX;

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
    public GameplayState(GameContext gameContext) {
        AssetManager assets = gameContext.getAssets();

        viewport = new Viewport();
        Level level = assets.getLevel(1);

        levelRenderer = new LevelRenderer(assets, level);
        font = new Font(assets);
        hud = new Hud(assets, font);
        spriteRenderer = new SpriteRenderer(assets);
        collision = new Collision();
        context = new GameplayContext(new Player(), level, new ActiveManager(), gameContext.getSoundManager());
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

        level.getActives().forEach(active -> context.getActiveManager().spawn(active));

        player.setX(level.getPlayerStartX());
        player.setY(level.getPlayerStartY());
    }

    @Override
    public void update(GameContext gameContext) {
        updatePlayer(gameContext.getKeyHandler().getInput());
        context.getActiveManager().update(context);
    }

    private void updatePlayer(KeyHandler.Input input) {
        Player player = context.getPlayer();

        player.processInput(input);
        player.update(context);
        collision.resolve(player, context.getLevel());

        if (player.isFiring()) {
            context.getActiveManager().spawn(Bolt.create(player));
            context.getSoundManager().play(SFX_BOLT_INDEX);
        }

        viewport.update(player.getX(), player.getY(), player.isGrounded());
    }

    @Override
    public void render(GameContext gameContext) {
        Renderer renderer = gameContext.getRenderer();
        levelRenderer.render(renderer, viewport);
        context.getActiveManager().render(renderer, spriteRenderer, viewport, Layer.BACKGROUND);
        drawPlayer(renderer);
        context.getActiveManager().render(renderer, spriteRenderer, viewport, Layer.FOREGROUND);
        hud.render(renderer, 0, context.getPlayer().getHealth());

        drawDebugInfo(renderer);
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

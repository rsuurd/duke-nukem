package duke;

import duke.active.Exit;
import duke.active.Lock;
import duke.active.Tv;
import duke.modals.Modal;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Deque;
import java.util.Iterator;

public class Gfx extends Canvas implements Renderer {
    private static final int WIDTH = 320;
    private static final int HEIGHT = 200;
    private static final int SCALE = 3;

    public static final int TILE_SIZE = 16;

    private Assets assets;

    private BufferedImage buffer;

    private Hud hud;
    private Font font;

    private int flasher;

    private Viewport viewport;

    public Gfx(ResourceLoader loader) {
        assets = new Assets(loader);
        font = new Font(assets);
        hud = new Hud(font, assets);

        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    public void init() {
        assets.init();
    }

    public void render(GameState gameState) {
        this.viewport = gameState.getViewport();
        Graphics graphics = buffer.getGraphics();

        clearScreen(graphics);
        drawLevel(gameState, graphics);

        Duke duke = gameState.getDuke();
        duke.render(this, assets);
        graphics.setColor(Color.magenta);
        graphics.drawRect(duke.getX() - viewport.getX(), duke.getY() - viewport.getY(), duke.getWidth(), duke.getHeight());
        gameState.getLevel().getActives().forEach(active -> active.render(this, assets));
        gameState.getBolts().forEach(bolt -> bolt.render(this, assets));
        gameState.getEffects().forEach(effect -> effect.render(this, assets));

        hud.draw(gameState, graphics);

        flip();
    }

    public void render(Deque<Modal> modals) {
        Iterator<Modal> iterator = modals.descendingIterator();

        while (iterator.hasNext()) {
            Modal modal = iterator.next();

            font.drawTextbox(modal.getText(), buffer.getGraphics(), modal.getX(), modal.getY(), modal.isPrompt() && !iterator.hasNext());
        }

        flip();
    }

    private void clearScreen(Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void drawLevel(GameState gameState, Graphics graphics) {
        Level level = gameState.getLevel();

        if (level.getBackdrop() != null) {
            graphics.drawImage(level.getBackdrop(), TILE_SIZE, TILE_SIZE, null);
        }

        int gridX = viewport.getX() / TILE_SIZE;
        int gridY = viewport.getY() / TILE_SIZE;

        for (int row = gridY; row < (gridY + 12); row++) {
            for (int col = gridX; col < (gridX + 15); col++) {
                int tileId = level.getTile(row, col);

                drawTile(resolveTile(tileId), (col * TILE_SIZE), (row * TILE_SIZE));
            }
        }

        flasher = (flasher + 1) % 4;
    }

    private BufferedImage resolveTile(int tileId) {
        BufferedImage image = null;

        if (tileId > 0x0 && tileId < 0x0600) {
            image = assets.getTileSet((tileId / 32) + flasher);
        } else if ((tileId >= 0x600) && (tileId < 0x3000)) {
            image = assets.getTileSet(tileId / 32);
        } else if (tileId == 0x3025) { // Brown spikes
            image = assets.getAnim(211);
        } else if (tileId == 0x3026) { // Rock (left)
            image = assets.getAnim(212);
        } else if (tileId == 0x3027) { // Rock (right)
            image = assets.getAnim(213);
        } else if (tileId == 0x3028) { // Little window
            image = assets.getAnim(214);
        } else if (tileId == 0x303D) { // Mesh
            image = assets.getAnim(262);
        } else if (tileId == 0x303E) { // Window (left)
            image = assets.getAnim(263);
        } else if (tileId == 0x303F) { // Window (right)
            image = assets.getAnim(264);
        } else if (tileId == 0x3040) { // Message
            image = assets.getObject(123);
        } else if (tileId == Lock.OFF_LOCK_TILE_ID) {
            image = assets.getObject(136);
        } else if (tileId == Lock.RED_LOCK_TILE_ID) {
            image = assets.getObject(137);
        } else if (tileId == Lock.GREEN_LOCK_TILE_ID) {
            image = assets.getObject(138);
        } else if (tileId == Lock.BLUE_LOCK_TILE_ID) {
            image = assets.getObject(139);
        } else if (tileId == Lock.MAGENTA_LOCK_TILE_ID) {
            image = assets.getObject(140);
        } else if ((tileId >= Exit.EXIT_DOOR_TILE_ID) && (tileId <= Exit.EXIT_DOOR_TILE_ID + 16)) {
            image = assets.getAnim(100 + tileId - Exit.EXIT_DOOR_TILE_ID);
        } else if ((tileId >= Tv.TV_TILE_ID) && (tileId <= Tv.TV_TILE_ID + 14)) {
            image = assets.getAnim(266 + tileId - Tv.TV_TILE_ID);
        }

        return image;
    }

    @Override
    public void drawTile(BufferedImage image, int x, int y) {
        int screenX = x - viewport.getX();
        int screenY = y - viewport.getY();

        if ((screenX >= 0) && (screenX < 224) && (screenY >= 0) && (screenY < 176)) {
            buffer.getGraphics().drawImage(image, screenX, screenY, null);
        }
    }

    private void flip() {
        BufferStrategy strategy = getBufferStrategy();

        if (strategy == null) {
            createBufferStrategy(3);
        } else {
            Graphics graphics = strategy.getDrawGraphics();

            graphics.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);

            graphics.dispose();
            strategy.show();
        }
    }
}

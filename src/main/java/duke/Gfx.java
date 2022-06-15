package duke;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Gfx extends Canvas implements Renderer {
    private static final int WIDTH = 320;
    private static final int HEIGHT = 200;
    private static final int SCALE = 3;

    public static final int TILE_SIZE = 16;
    public static final int LEFT_BOUND = 88;
    public static final int RIGHT_BOUND = 120;
    public static final int UPPER_BOUND = 48;
    public static final int LOWER_BOUND = 112;
    public static final int VERTICAL_CENTER = 96;

    private ResourceLoader loader;
    private Assets assets;

    private BufferedImage buffer;

    private Hud hud;
    private Font font;

    private int flasher;

    private int cameraX;
    private int cameraY;

    public Gfx(ResourceLoader loader) {
        this.loader = loader;
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
        moveCamera(gameState);

        Graphics graphics = buffer.getGraphics();

        clearScreen(graphics);
        drawLevel(gameState, graphics);

        Duke duke = gameState.getDuke();
        duke.render(this, assets);
        graphics.setColor(Color.magenta);
        graphics.drawRect(duke.getX() - cameraX, duke.getY() - cameraY, duke.getWidth(), duke.getHeight());
        gameState.getLevel().getActives().forEach(active -> active.render(this, assets));
        gameState.getBolts().forEach(bolt -> bolt.render(this, assets));
        gameState.getEffects().forEach(effect -> effect.render(this, assets));

        hud.draw(gameState, graphics);

        flip();
    }

    private void moveCamera(GameState gameState) {
        Duke duke = gameState.getDuke();

        int screenX = duke.getX() - cameraX;

        if (screenX < LEFT_BOUND) {
            cameraX = duke.getX() - LEFT_BOUND;
        } else if (screenX > RIGHT_BOUND) {
            cameraX = duke.getX() - RIGHT_BOUND;
        }

        int screenY = duke.getY() - cameraY;

        if (screenY < UPPER_BOUND) {
            cameraY = duke.getY() - UPPER_BOUND;
        } else if (screenY > LOWER_BOUND) {
            cameraY = duke.getY() - LOWER_BOUND;
        }

        if ((duke.getState() == Duke.State.STAND) || (duke.getState() == Duke.State.WALK)) {
            cameraY += (16 * Integer.signum(screenY - VERTICAL_CENTER));
        }
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

        int gridX = cameraX / TILE_SIZE;
        int gridY = cameraY / TILE_SIZE;

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
        }

        return image;
    }

    @Override
    public void drawTile(BufferedImage image, int x, int y) {
        int screenX = x - cameraX;
        int screenY = y - cameraY;

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

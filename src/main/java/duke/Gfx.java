package duke;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.List;

public class Gfx extends Canvas {
    private static final int WIDTH = 320;
    private static final int HEIGHT = 200;

    private static final int SCALE = 3;

    public static final int TILE_SIZE = 16;

    private ResourceLoader loader;

    private BufferedImage buffer;

    private List<BufferedImage> tileSet;
    private List<BufferedImage> man;

    public Gfx(ResourceLoader loader) {
        this.loader = loader;

        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    public void init() {
        tileSet = loader.readTiles();
        man = loader.readMan();
    }

    public void render(GameState gameState) {
        Graphics graphics = buffer.getGraphics();

        clearScreen(graphics);
        drawLevel(gameState, graphics);
        drawDuke(gameState, graphics);
        drawBoundingBox(gameState, graphics);

        flip();
    }

    private void clearScreen(Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void drawLevel(GameState gameState, Graphics graphics) {
        if (gameState.getLevel().getBackdrop() != null) {
            graphics.drawImage(gameState.getLevel().getBackdrop(), 0, 0, null);
        }

        int gridX = gameState.getCameraX() / TILE_SIZE;
        int gridY = gameState.getCameraY() / TILE_SIZE;

        int scrollX = gameState.getCameraX() % TILE_SIZE;
        int scrollY = gameState.getCameraY() % TILE_SIZE;

        int screenY = -scrollY;
        for (int row = gridY; row < (gridY + 12); row++) {
            int screenX = -scrollX;

            for (int col = gridX; col < (gridX + 15); col++) {
                int tileId = gameState.getLevel().getTile(row, col);

                if (tileId > 0 && tileId < 0x3000) {
                    graphics.drawImage(tileSet.get(tileId / 32), screenX, screenY, null);
                }

                screenX += TILE_SIZE;
            }

            screenY += TILE_SIZE;
        }
    }

    private void drawDuke(GameState gameState, Graphics graphics) {
        Duke duke = gameState.getDuke();

        // walking left: 0
        // walking right: 16
        // jump left: 32
        // jump right: 36
        // fall left: 40
        // fall right: 44
        // stand left: 50
        // stand right: 54

        int tileIndex = switch (duke.getState()) {
            case STAND -> 50;
            case WALK -> 0;
            case JUMP -> 32;
            case FALL -> 40;
        };

        tileIndex += (duke.getFacing() == Facing.LEFT) ? 0 : (duke.getState() == Duke.State.WALK) ? 16 : 4;

        tileIndex += ((duke.getFrame() / 2) * 4);

        graphics.drawImage(man.get(tileIndex), duke.getX() - gameState.getCameraX(), duke.getY() - gameState.getCameraY(), null);
        graphics.drawImage(man.get(tileIndex + 1), duke.getX() - gameState.getCameraX() + TILE_SIZE, duke.getY() - gameState.getCameraY(), null);
        graphics.drawImage(man.get(tileIndex + 2), duke.getX() - gameState.getCameraX(), duke.getY() - gameState.getCameraY() + TILE_SIZE, null);
        graphics.drawImage(man.get(tileIndex + 3), duke.getX() - gameState.getCameraX() + TILE_SIZE, duke.getY() - gameState.getCameraY() + TILE_SIZE, null);
    }

    private void drawBoundingBox(GameState gameState, Graphics graphics) {
        Duke duke = gameState.getDuke();

        if (gameState.getLevel().collides(duke.getX(), duke.getY(), 31, 31)) {
            graphics.setColor(Color.red);
        } else {
            graphics.setColor(Color.magenta);
        }

        graphics.drawRect(duke.getX() - gameState.getCameraX(), duke.getY() - gameState.getCameraY(), 31, 31);
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

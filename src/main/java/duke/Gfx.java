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

    public Gfx(ResourceLoader loader) {
        this.loader = loader;

        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    public void init() {
        tileSet = loader.readTiles();
    }

    public void render(GameState gameState) {
        Graphics graphics = buffer.getGraphics();
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

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

        flip();
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

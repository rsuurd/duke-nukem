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
    public static final int LEFT_BOUND = 88;
    public static final int RIGHT_BOUND = 120;
    public static final int UPPER_BOUND = 48;
    public static final int LOWER_BOUND = 112;
    public static final int VERTICAL_CENTER = 96;

    private ResourceLoader loader;

    private BufferedImage buffer;

    private List<BufferedImage> tileSet;
    private List<BufferedImage> man;
    private Hud hud;

    private int flasher;
    
    private int cameraX;
    private int cameraY;

    public Gfx(ResourceLoader loader) {
        this.loader = loader;

        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        hud = new Hud(loader);
    }

    public void init() {
        tileSet = loader.readTiles();
        man = loader.readMan();
        hud.init();
    }

    public void render(GameState gameState) {
        moveCamera(gameState);

        Graphics graphics = buffer.getGraphics();

        clearScreen(graphics);
        drawLevel(gameState, graphics);
        drawDuke(gameState, graphics);
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
        if (gameState.getLevel().getBackdrop() != null) {
            graphics.drawImage(gameState.getLevel().getBackdrop(), TILE_SIZE, TILE_SIZE, null);
        }

        int gridX = cameraX / TILE_SIZE;
        int gridY = cameraY / TILE_SIZE;

        int scrollX = cameraX % TILE_SIZE;
        int scrollY = cameraY % TILE_SIZE;

        int screenY = -scrollY;
        for (int row = gridY; row < (gridY + 12); row++) {
            int screenX = -scrollX;

            for (int col = gridX; col < (gridX + 15); col++) {
                int tileId = gameState.getLevel().getTile(row, col);

                if (tileId > 0 && tileId < 0x3000) {
                    int index = (tileId / 32) + ((tileId < 0x0600) ? flasher : 0);

                    graphics.drawImage(tileSet.get(index), screenX, screenY, null);
                }

                screenX += TILE_SIZE;
            }

            screenY += TILE_SIZE;
        }

        flasher = (flasher + 1) % 4;
    }

    private void drawDuke(GameState gameState, Graphics graphics) {
        Duke duke = gameState.getDuke();

        int tileIndex = switch (duke.getState()) {
            case STAND -> 50;
            case WALK -> 0;
            case JUMP -> 32;
            case FALL -> 40;
        };

        tileIndex += (duke.getFacing() == Facing.LEFT) ? 0 : (duke.getState() == Duke.State.WALK) ? 16 : 4;

        tileIndex += ((duke.getFrame() / 2) * 4);

        graphics.drawImage(man.get(tileIndex), duke.getX() - cameraX, duke.getY() - cameraY, null);
        graphics.drawImage(man.get(tileIndex + 1), duke.getX() - cameraX + TILE_SIZE, duke.getY() - cameraY, null);
        graphics.drawImage(man.get(tileIndex + 2), duke.getX() - cameraX, duke.getY() - cameraY + TILE_SIZE, null);
        graphics.drawImage(man.get(tileIndex + 3), duke.getX() - cameraX + TILE_SIZE, duke.getY() - cameraY + TILE_SIZE, null);

        graphics.setColor(Color.magenta);
        graphics.drawRect(duke.getX() - cameraX, duke.getY() - cameraY, 31, 31);
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

package duke;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Gfx extends Canvas {
    private static final int WIDTH = 320;
    private static final int HEIGHT = 200;

    private static final int SCALE = 3;

    private ResourceLoader loader;

    private BufferedImage buffer;

    private List<BufferedImage> tileSet;

    public Gfx(ResourceLoader loader) {
        this.loader = loader;

        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    public void init() {
        tileSet = Stream.of("BACK0.DN1", "BACK1.DN1", "BACK2.DN1", "BACK3.DN1",
                "SOLID0.DN1", "SOLID1.DN1", "SOLID2.DN1", "SOLID3.DN1").flatMap(name -> loader.readTiles(name).stream().limit(48)).collect(Collectors.toList());
    }

    public void render(GameState gameState) {
        Graphics graphics = buffer.getGraphics();
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 13; col++) {
                int tileId = gameState.getLevel().getTile(row + (gameState.getCameraY() / 16), col + (gameState.getCameraX() / 16));

                if (tileId < 0x3000) {

                    graphics.drawImage(tileSet.get(tileId / 32), col * 16, row * 16, null);
                }
            }
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

package duke.ui;

import duke.Renderer;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class CanvasRenderer extends Canvas implements Renderer {
    private static final int WIDTH = 320;
    private static final int HEIGHT = 200;
    private static final int SCALE = 3;

    private BufferedImage buffer;

    public CanvasRenderer() {
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void clear() {
        Graphics graphics = buffer.getGraphics();
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
    }

    @Override
    public void flip() {
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

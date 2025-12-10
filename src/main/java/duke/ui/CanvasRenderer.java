package duke.ui;

import duke.Renderer;
import duke.gfx.Sprite;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class CanvasRenderer extends Canvas implements Renderer {
    private static final int WIDTH = 320;
    private static final int HEIGHT = 200;
    private static final int SCALE = 3;

    private Sprite buffer;

    public CanvasRenderer() {
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        buffer = new Sprite(320, 200);
    }

    @Override
    public void clear() {
        buffer.clear();
    }

    @Override
    public void flip() {
        BufferStrategy strategy = getBufferStrategy();

        if (strategy == null) {
            createBufferStrategy(3);
        } else {
            Graphics graphics = strategy.getDrawGraphics();

            // TODO convert sprite to image using EgaPalette
//            graphics.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);

            graphics.dispose();
            strategy.show();
        }
    }
}

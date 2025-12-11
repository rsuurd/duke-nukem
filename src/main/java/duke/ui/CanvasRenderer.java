package duke.ui;

import duke.Renderer;
import duke.gfx.EgaPalette;
import duke.gfx.Sprite;

import java.awt.*;
import java.awt.image.*;

public class CanvasRenderer extends Canvas implements Renderer {
    private static final int WIDTH = 320;
    private static final int HEIGHT = 200;
    private static final int SCALE = 3;

    private Sprite buffer;

    private byte[] pixelData;
    private WritableRaster raster;
    private BufferedImage indexedImage;

    public CanvasRenderer(EgaPalette palette) {
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        palette.addListener(() -> indexedImage = new BufferedImage(palette.getColorModel(), raster, false, null));

        buffer = new Sprite(WIDTH, HEIGHT);

        raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, buffer.getWidth(), buffer.getHeight(), 1, null);
        pixelData = ((DataBufferByte) raster.getDataBuffer()).getData();
        indexedImage = new BufferedImage(palette.getColorModel(), raster, false, null);
    }

    @Override
    public void clear() {
        buffer.clear();
    }

    @Override
    public void draw(Sprite sprite, int x, int y) {
        buffer.draw(sprite, x, y);
    }

    @Override
    public void flip() {
        BufferStrategy strategy = getBufferStrategy();

        if (strategy == null) {
            createBufferStrategy(3);
        } else {
            Graphics graphics = strategy.getDrawGraphics();

            byte[] pixels = buffer.getPixels();
            System.arraycopy(pixels, 0, pixelData, 0, pixels.length);
            graphics.drawImage(indexedImage, 0, 0, getWidth(), getHeight(), null);
            graphics.dispose();
            strategy.show();
        }
    }
}

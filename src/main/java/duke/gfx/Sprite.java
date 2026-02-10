package duke.gfx;

import java.util.Arrays;

public class Sprite {
    private int width;
    private int height;
    private byte[] pixels;

    public Sprite(int width, int height) {
        this(width, height, new byte[width * height]);
    }

    public Sprite(int width, int height, byte[] pixels) {
        if (pixels.length != width * height) {
            throw new IllegalArgumentException("Sprite pixels do not match given dimensions");
        }

        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public byte getPixel(int x, int y) {
        return pixels[y * width + x];
    }

    public void setPixel(int x, int y, byte pixel) {
        pixels[y * width + x] = pixel;
    }

    public byte[] getPixels() {
        return pixels;
    }

    public void draw(Sprite sprite, int x, int y) {
        int spriteWidth = sprite.getWidth();
        int startX = Math.max(0, -x);
        int startY = Math.max(0, -y);
        int endX = Math.min(spriteWidth, width - x);
        int endY = Math.min(sprite.getHeight(), height - y);

        if (startX >= endX || startY >= endY) {
            return;
        }

        byte[] spritePixels = sprite.pixels;

        for (int yy = startY; yy < endY; yy++) {
            int spriteRow = yy * spriteWidth;
            int row = (yy + y) * width;

            for (int xx = startX; xx < endX; xx++) {
                byte index = spritePixels[spriteRow + xx];

                if (index != (byte) 0xff) {
                    pixels[row + xx + x] = index;
                }
            }
        }
    }

    public void clear() {
        Arrays.fill(pixels, (byte) 0);
    }
}

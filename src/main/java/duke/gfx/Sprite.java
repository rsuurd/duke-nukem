package duke.gfx;

import java.util.Arrays;

public class Sprite {
    private int width;
    private int height;
    private int pixels[];

    public Sprite(int width, int height) {
        this(width, height, new int[width * height]);
    }

    public Sprite(int width, int height, int[] pixels) {
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

    public int getPixel(int x, int y) {
        return pixels[y * width + x];
    }

    public void draw(Sprite sprite, int x, int y) {
        int spiteWidth = sprite.getWidth();
        int startX = Math.max(0, -x);
        int startY = Math.max(0, -y);
        int endX = Math.min(spiteWidth, width - x);
        int endY = Math.min(sprite.getHeight(), height - y);

        if (startX >= endX || startY >= endY) {
            return;
        }

        int[] spritePixels = sprite.pixels;

        for (int yy = startY; yy < endY; yy++) {
            int spriteRow = yy * spiteWidth;
            int row = (yy + y) * width;

            for (int xx = startX; xx < endX; xx++) {
                int index = spritePixels[spriteRow + xx];

                if (index != 0) {
                    pixels[row + xx + x] = index;
                }
            }
        }
    }

    public void clear() {
        Arrays.fill(pixels, 0);
    }
}

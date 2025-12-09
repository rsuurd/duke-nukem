package duke.gfx;

public class Sprite {
    private int width;
    private int height;
    private int pixels[];

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
}

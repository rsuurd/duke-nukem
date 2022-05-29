package duke;

import java.awt.image.BufferedImage;

public interface Renderer {
    void drawTile(BufferedImage image, int x, int y);
}

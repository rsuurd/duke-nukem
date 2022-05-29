package duke;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Font {
    private static final int FONT_SIZE = 8;

    private List<BufferedImage> fonts;

    public Font(List<BufferedImage> fonts) {
        this.fonts = fonts;
    }

    public void drawText(String text, Graphics graphics, int x, int y) {
        int screenX = x;
        int screenY = y;

        for (char c : text.toCharArray()) {
            if (c == '\n') {
                screenY += FONT_SIZE;
                screenX = x;
            } else {
                int index = c - 22 - (Character.isLowerCase(c) ? 6 : 0);

                graphics.drawImage(fonts.get(index), screenX, screenY, null);

                screenX += FONT_SIZE;
            }
        }
    }
}

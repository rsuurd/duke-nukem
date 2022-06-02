package duke;

import java.awt.*;

public class Font {
    private static final int FONT_SIZE = 8;

    private Assets assets;

    public Font(Assets assets) {
        this.assets = assets;
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

                graphics.drawImage(assets.getFont(index), screenX, screenY, null);

                screenX += FONT_SIZE;
            }
        }
    }
}

package duke;

import java.awt.*;

import static duke.Gfx.TILE_SIZE;

public class Font {
    private static final int FONT_SIZE = 8;

    private Assets assets;

    private int frame;

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

    public void drawTextbox(String text, Graphics graphics, int x, int y, boolean prompt) {
        int rows = (text.split("\n").length + 1) / 2;

        int l, m, r;

        for (int i = 0; i <= rows; i++) {
            int screenY = y + (i * TILE_SIZE);

            if (i == 0) {
                l = 18;
                m = 24;
                r = 19;
            } else if (i == rows) {
                l = 20;
                m = 25;
                r = 21;
            } else {
                l = 22;
                m = 17;
                r = 23;
            }


            graphics.drawImage(assets.getBorder(l), x,  screenY, null);
            for (int j = 1; j < 12; j ++) {
                graphics.drawImage(assets.getBorder(m), x + (j * TILE_SIZE), screenY, null);
            }
            graphics.drawImage(assets.getBorder(r), x + (12 * TILE_SIZE), screenY, null);

        }

        drawText(text, graphics, x + FONT_SIZE, y + FONT_SIZE);

        if (prompt) {
            graphics.drawImage(assets.getObject(85 + frame), 13 * TILE_SIZE, y + (rows * TILE_SIZE), null);

            frame = (frame + 1) % 4;
        }
    }
}

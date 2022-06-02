package duke;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static duke.Gfx.TILE_SIZE;

public class Hud {
    private Font font;
    private Assets assets;

    public Hud(Font font, Assets assets) {
        this.font = font;
        this.assets = assets;
    }

    public void draw(GameState gameState, Graphics graphics) {
        // bolts
        graphics.drawImage(assets.getBorder(4), 0, 0, null);
        graphics.drawImage(assets.getBorder(7), 0, 176, null);
        graphics.drawImage(assets.getBorder(9), 304, 0, null);
        graphics.drawImage(assets.getBorder(11), 304, 176, null);
        graphics.drawImage(assets.getBorder(5), 224, 0, null);
        graphics.drawImage(assets.getBorder(6), 224, 176, null);

        // strips
        for (int i = 1; i < 14; i++) {
            // horizontal
            graphics.drawImage(assets.getBorder(2), i * TILE_SIZE, 0, null);
            graphics.drawImage(assets.getBorder(3), i * TILE_SIZE, 176, null);

            if (i < 11) { // vertical
                graphics.drawImage(assets.getBorder(0), 0, i * TILE_SIZE, null);
                graphics.drawImage(assets.getBorder(1), 224, i * TILE_SIZE, null);
                graphics.drawImage(assets.getBorder(10), 304, i * TILE_SIZE, null);
            }
        }

        // score
        graphics.drawImage(assets.getBorder(8), 240, 0, null);
        graphics.drawImage(assets.getBorder(38), 256, 0, null);
        graphics.drawImage(assets.getBorder(39), 272, 0, null);
        graphics.drawImage(assets.getBorder(8), 288, 0, null);

        font.drawText(String.format("%08d", gameState.getScore()), graphics, 240, 24);

        // health
        graphics.drawImage(assets.getBorder(14), 224, 40, null);
        graphics.drawImage(assets.getBorder(8), 240, 40, null);
        graphics.drawImage(assets.getBorder(36), 256, 40, null); // TODO blink red
        graphics.drawImage(assets.getBorder(37), 272, 40, null); // TODO blink red
        graphics.drawImage(assets.getBorder(8), 288, 40, null);
        graphics.drawImage(assets.getBorder(15), 304, 40, null);

        // fire power
        graphics.drawImage(assets.getBorder(14), 224, 80, null);
        graphics.drawImage(assets.getBorder(8), 240, 80, null);
        graphics.drawImage(assets.getBorder(8), 288, 80, null);
        graphics.drawImage(assets.getBorder(33), 248, 80, null);
        graphics.drawImage(assets.getBorder(34), 264, 80, null);
        graphics.drawImage(assets.getBorder(35), 280, 80, null);
        graphics.drawImage(assets.getBorder(15), 304, 80, null);

        // inventory
        graphics.drawImage(assets.getBorder(14), 224, 128, null);
        graphics.drawImage(assets.getBorder(8), 240, 128, null);
        graphics.drawImage(assets.getBorder(8), 288, 128, null);
        graphics.drawImage(assets.getBorder(30), 248, 128, null);
        graphics.drawImage(assets.getBorder(31), 264, 128, null);
        graphics.drawImage(assets.getBorder(32), 280, 128, null);
        graphics.drawImage(assets.getBorder(15), 304, 128, null);
        graphics.drawImage(assets.getBorder(8), 240, 176, null);
        graphics.drawImage(assets.getBorder(8), 256, 176, null);
        graphics.drawImage(assets.getBorder(8), 272, 176, null);
        graphics.drawImage(assets.getBorder(8), 288, 176, null);

        // press f1 for help
        graphics.drawImage(assets.getBorder(26), 88, 176, null);
        graphics.drawImage(assets.getBorder(27), 104, 176, null);
        graphics.drawImage(assets.getBorder(28), 120, 176, null);
        graphics.drawImage(assets.getBorder(29), 137, 176, null);
    }
}

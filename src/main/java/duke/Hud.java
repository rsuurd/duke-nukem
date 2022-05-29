package duke;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static duke.Gfx.TILE_SIZE;

public class Hud {
    private Font font;
    private List<BufferedImage> borders;

    public Hud(Font font, List<BufferedImage> borders) {
        this.font = font;
        this.borders = borders;
    }

    public void draw(GameState gameState, Graphics graphics) {
        // bolts
        graphics.drawImage(borders.get(4), 0, 0, null);
        graphics.drawImage(borders.get(7), 0, 176, null);
        graphics.drawImage(borders.get(9), 304, 0, null);
        graphics.drawImage(borders.get(11), 304, 176, null);
        graphics.drawImage(borders.get(5), 224, 0, null);
        graphics.drawImage(borders.get(6), 224, 176, null);

        // strips
        for (int i = 1; i < 14; i++) {
            // horizontal
            graphics.drawImage(borders.get(2), i * TILE_SIZE, 0, null);
            graphics.drawImage(borders.get(3), i * TILE_SIZE, 176, null);

            if (i < 11) { // vertical
                graphics.drawImage(borders.get(0), 0, i * TILE_SIZE, null);
                graphics.drawImage(borders.get(1), 224, i * TILE_SIZE, null);
                graphics.drawImage(borders.get(10), 304, i * TILE_SIZE, null);
            }
        }

        // score
        graphics.drawImage(borders.get(8), 240, 0, null);
        graphics.drawImage(borders.get(38), 256, 0, null);
        graphics.drawImage(borders.get(39), 272, 0, null);
        graphics.drawImage(borders.get(8), 288, 0, null);

        font.drawText(String.format("%08d", gameState.getScore()), graphics, 240, 24);

        // health
        graphics.drawImage(borders.get(14), 224, 40, null);
        graphics.drawImage(borders.get(8), 240, 40, null);
        graphics.drawImage(borders.get(36), 256, 40, null); // TODO blink red
        graphics.drawImage(borders.get(37), 272, 40, null); // TODO blink red
        graphics.drawImage(borders.get(8), 288, 40, null);
        graphics.drawImage(borders.get(15), 304, 40, null);

        // fire power
        graphics.drawImage(borders.get(14), 224, 80, null);
        graphics.drawImage(borders.get(8), 240, 80, null);
        graphics.drawImage(borders.get(8), 288, 80, null);
        graphics.drawImage(borders.get(33), 248, 80, null);
        graphics.drawImage(borders.get(34), 264, 80, null);
        graphics.drawImage(borders.get(35), 280, 80, null);
        graphics.drawImage(borders.get(15), 304, 80, null);

        // inventory
        graphics.drawImage(borders.get(14), 224, 128, null);
        graphics.drawImage(borders.get(8), 240, 128, null);
        graphics.drawImage(borders.get(8), 288, 128, null);
        graphics.drawImage(borders.get(30), 248, 128, null);
        graphics.drawImage(borders.get(31), 264, 128, null);
        graphics.drawImage(borders.get(32), 280, 128, null);
        graphics.drawImage(borders.get(15), 304, 128, null);
        graphics.drawImage(borders.get(8), 240, 176, null);
        graphics.drawImage(borders.get(8), 256, 176, null);
        graphics.drawImage(borders.get(8), 272, 176, null);
        graphics.drawImage(borders.get(8), 288, 176, null);

        // press f1 for help
        graphics.drawImage(borders.get(26), 88, 176, null);
        graphics.drawImage(borders.get(27), 104, 176, null);
        graphics.drawImage(borders.get(28), 120, 176, null);
        graphics.drawImage(borders.get(29), 137, 176, null);
    }
}

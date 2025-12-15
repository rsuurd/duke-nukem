package duke.gfx;

import duke.Renderer;
import duke.resources.AssetManager;

public class Font {
    private static final int FONT_SIZE = 8;

    private AssetManager assets;

    public Font(AssetManager assets) {
        this.assets = assets;
    }

    public void drawText(Renderer renderer, String text, int x, int y) {
        int screenX = x;
        int screenY = y;

        for (char c : text.toCharArray()) {
            if (c == '\n') {
                screenY += FONT_SIZE;
                screenX = x;
            } else {
                int index = c - 22 - (Character.isLowerCase(c) ? 6 : 0);

                Sprite sprite = assets.getFont().get(index);

                renderer.draw(sprite, screenX, screenY);

                screenX += FONT_SIZE;
            }
        }
    }
}

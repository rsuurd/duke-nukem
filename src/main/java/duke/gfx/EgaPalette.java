package duke.gfx;

import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.List;

public class EgaPalette {
    private IndexColorModel colorModel;
    private List<PaletteChangedListener> listeners;
    private byte[][] palettes;
    private int brightness;

    public EgaPalette() {
        listeners = new ArrayList<>();
        palettes = new byte[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 4, 4, 8, 0, 1, 2, 3, 4, 5, 6, 7},
                {0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 7},
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
        };
        reset();
    }

    public void addListener(PaletteChangedListener listener) {
        listeners.add(listener);
    }

    public IndexColorModel getColorModel() {
        return colorModel;
    }

    public void fadeIn() {
        fade(true);
    }

    public void fadeOut() {
        fade(false);
    }

    private void fade(boolean in) {
        if (in && brightness < palettes.length - 1) {
            brightness++;
            rebuildColorModel();
        } else if (!in && brightness > 0) {
            brightness--;
            rebuildColorModel();
        }
    }

    public void reset() {
        brightness = palettes.length - 1;
        rebuildColorModel();
    }

    public void blackout() {
        brightness = 0;
        rebuildColorModel();
    }

    private void rebuildColorModel() {
        byte[] r = new byte[16];
        byte[] g = new byte[16];
        byte[] b = new byte[16];
        byte[] palette = palettes[brightness];

        for (int i = 0; i < palette.length; i++) {
            int colorIndex = palette[i] & 0xFF;
            int color = COLORS[colorIndex];
            r[i] = (byte) ((color >> 16) & 0xFF);
            g[i] = (byte) ((color >> 8) & 0xFF);
            b[i] = (byte) (color & 0xFF);
        }

        colorModel = new IndexColorModel(8, 16, r, g, b);
        listeners.forEach(PaletteChangedListener::onPaletteChanged);
    }

    private static final int[] COLORS = new int[]{
            0xFF000000, // 0 black
            0xFF0000AA, // 1 blue
            0xFF00AA00, // 2 green
            0xFF00AAAA, // 3 cyan
            0xFFAA0000, // 4 red
            0xFFAA00AA, // 5 magenta
            0xFFAA5500, // 6 brown
            0xFFAAAAAA, // 7 light gray
            0xFF555555, // 8 dark gray
            0xFF5555FF, // 9 bright blue
            0xFF55FF55, // 10 bright green
            0xFF55FFFF, // 11 bright cyan
            0xFFFF5555, // 12 bright red
            0xFFFF55FF, // 13 bright magenta
            0xFFFFFF55, // 14 yellow
            0xFFFFFFFF  // 15 white
    };

    public interface PaletteChangedListener {
        void onPaletteChanged();
    }
}

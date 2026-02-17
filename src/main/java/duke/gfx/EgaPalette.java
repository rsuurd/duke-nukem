package duke.gfx;

import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.List;

public class EgaPalette {
    private Mode mode;
    private int brightness;

    private IndexColorModel colorModel;

    private List<PaletteChangedListener> listeners;

    public EgaPalette() {
        listeners = new ArrayList<>();

        reset();
    }

    public void fadeIn() {
        if (brightness > 0) {
            blackout();
        }

        mode = Mode.FADE_FROM_BLACK;
    }

    public void fadeOut() {
        if (brightness < MAX_BRIGHTNESS) {
            reset();
        }

        mode = Mode.FADE_TO_BLACK;
    }

    public void update() {
        switch (mode) {
            case NONE -> {
            }
            case FADE_FROM_BLACK -> increaseBrightness();
            case FADE_TO_BLACK -> decreaseBrightness();
        }
    }

    private void increaseBrightness() {
        if (brightness < MAX_BRIGHTNESS) {
            brightness++;
            rebuildColorModel();
        }
    }

    private void decreaseBrightness() {
        if (brightness > 0) {
            brightness--;
            rebuildColorModel();
        }
    }

    public void reset() {
        mode = Mode.NONE;
        brightness = MAX_BRIGHTNESS;
        rebuildColorModel();
    }

    public void blackout() {
        brightness = 0;
        rebuildColorModel();
    }

    public boolean isFadedIn() {
        return brightness == MAX_BRIGHTNESS;
    }

    public boolean isFadedOut() {
        return brightness == 0;
    }

    private void rebuildColorModel() {
        int size = COLORS.length;
        byte[] r = new byte[size];
        byte[] g = new byte[size];
        byte[] b = new byte[size];

        int visibleColors = (COLORS.length / MAX_BRIGHTNESS) * brightness;

        for (int i = 0; i < size; i++) {
            // TODO support fading to white as well
            int color = (i < visibleColors) ? COLORS[i & 0xFF] : BLACK;

            r[i] = (byte) ((color >> 16) & 0xFF);
            g[i] = (byte) ((color >> 8) & 0xFF);
            b[i] = (byte) (color & 0xFF);
        }

        colorModel = new IndexColorModel(8, size, r, g, b);
        listeners.forEach(PaletteChangedListener::onPaletteChanged);
    }

    public IndexColorModel getColorModel() {
        return colorModel;
    }

    public void addListener(PaletteChangedListener listener) {
        listeners.add(listener);
    }

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private static final int[] COLORS = new int[]{
            BLACK,
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
            WHITE  // 15 white
    };

    private static final int MAX_BRIGHTNESS = 4;

    enum Mode {
        NONE,
        FADE_FROM_BLACK,
        FADE_TO_BLACK
    }

    public interface PaletteChangedListener {
        void onPaletteChanged();
    }
}

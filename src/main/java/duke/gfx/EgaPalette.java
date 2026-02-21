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

    public void fadeToWhite() {
        if (brightness > 0) {
            blackout();
        }

        mode = Mode.FADE_TO_WHITE;
    }

    public void fadeFromWhite() {
        if (brightness < MAX_BRIGHTNESS) {
            reset();
        }

        mode = Mode.FADE_FROM_WHITE;
    }

    public void update() {
        switch (mode) {
            case NONE -> {
            }
            case FADE_FROM_BLACK, FADE_TO_WHITE -> increaseBrightness();
            case FADE_TO_BLACK, FADE_FROM_WHITE -> decreaseBrightness();
        }
    }

    private void increaseBrightness() {
        if (brightness < MAX_BRIGHTNESS) {
            brightness++;

            // if (brightness == MAX_BRIGHTNESS) reset() else rebuild();
            rebuildColorModel();
        }
    }

    private void decreaseBrightness() {
        if (brightness > 0) {
            brightness--;

            // brightness == 0 reset(); else rebuild();
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

    public boolean isFadedBlack() {
        return switch (mode) {
            case NONE, FADE_TO_BLACK, FADE_FROM_BLACK -> brightness == 0;
            default -> false;
        };
    }

    public boolean isFadedWhite() {
        return switch (mode) {
            case FADE_TO_WHITE, FADE_FROM_WHITE -> brightness == MAX_BRIGHTNESS;
            default -> false;
        };
    }

    public boolean isFadedIn() {
        return switch (mode) {
            case NONE, FADE_FROM_BLACK, FADE_TO_BLACK -> brightness == MAX_BRIGHTNESS;
            case FADE_FROM_WHITE, FADE_TO_WHITE -> brightness == 0;
        };
    }

    private void rebuildColorModel() {
        int size = COLORS.length;
        byte[] r = new byte[size];
        byte[] g = new byte[size];
        byte[] b = new byte[size];

        for (int i = 0; i < size; i++) {
            int color = getColor(i);

            r[i] = (byte) ((color >> 16) & 0xFF);
            g[i] = (byte) ((color >> 8) & 0xFF);
            b[i] = (byte) (color & 0xFF);
        }

        colorModel = new IndexColorModel(8, size, r, g, b);
        listeners.forEach(PaletteChangedListener::onPaletteChanged);
    }

    private int getColor(int index) {
        int fadedColors = (COLORS.length / MAX_BRIGHTNESS) * brightness;

        return switch (mode) {
            case NONE, FADE_TO_BLACK, FADE_FROM_BLACK -> index < fadedColors ? COLORS[index & 0xFF] : BLACK;
            case FADE_TO_WHITE, FADE_FROM_WHITE -> index >= fadedColors ? COLORS[index & 0xFF] : WHITE;
        };
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
        FADE_TO_BLACK,
        FADE_FROM_WHITE,
        FADE_TO_WHITE
    }

    public interface PaletteChangedListener {
        void onPaletteChanged();
    }
}

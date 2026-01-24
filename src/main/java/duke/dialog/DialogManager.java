package duke.dialog;

import duke.GameContext;
import duke.Renderer;
import duke.gfx.Font;
import duke.gfx.Sprite;
import duke.resources.AssetManager;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static duke.level.Level.HALF_TILE_SIZE;
import static duke.level.Level.TILE_SIZE;
import static java.awt.event.KeyEvent.VK_ENTER;

public class DialogManager {
    private AssetManager assets;
    private Font font;
    private int tick;

    private Deque<Dialog> dialogs;

    public DialogManager(AssetManager assets, Font font) {
        this.assets = assets;
        this.font = font;
        this.tick = 0;

        dialogs = new LinkedList<>();
    }

    public void render(Renderer renderer) {
        if (dialogs.isEmpty()) return;

        Dialog dialog = dialogs.peek();

        renderBox(renderer, dialog.x(), dialog.y(), dialog.rows(), dialog.cols());
        font.drawText(renderer, dialog.message(), dialog.x() + HALF_TILE_SIZE, dialog.y() + TILE_SIZE);

        if (dialog.showCursor()) {
            renderCursor(renderer, dialog.x(), dialog.y(), dialog.rows(), dialog.cols());
        }
    }

    private void renderBox(Renderer renderer, int x, int y, int rows, int cols) {
        if (rows < 2 || cols < 2) {
            throw new IllegalArgumentException("rows and columns must be at least 2");
        }

        drawBorder(renderer, x, y, cols, TOP_LEFT_BORDER, TOP_BORDER, TOP_RIGHT_BORDER);
        y += TILE_SIZE;
        for (int row = 1; row < rows - 1; row++) {
            drawBorder(renderer, x, y, cols, LEFT_BORDER, BACKGROUND, RIGHT_BORDER);
            y += TILE_SIZE;
        }
        drawBorder(renderer, x, y, cols, BOTTOM_LEFT_BORDER, BOTTOM_BORDER, BOTTOM_RIGHT_BORDER);
    }

    private void drawBorder(Renderer renderer, int x, int y, int cols, int leftTile, int middleTile, int rightTile) {
        List<Sprite> border = assets.getBorder();

        int screenX = x;
        renderer.draw(border.get(leftTile), screenX, y);
        screenX += TILE_SIZE;
        for (int col = 1; col < cols - 1; col++) {
            renderer.draw(border.get(middleTile), screenX, y);
            screenX += TILE_SIZE;
        }
        renderer.draw(border.get(rightTile), screenX, y);
    }

    private void renderCursor(Renderer renderer, int x, int y, int rows, int cols) {
        renderer.draw(assets.getObjects().get(85 + tick), x + cols * TILE_SIZE - TILE_SIZE, y + rows * TILE_SIZE - TILE_SIZE);

        tick = (tick + 1) % 4;
    }

    public void open(Dialog dialog) {
        dialogs.push(dialog);
    }

    public void close() {
        dialogs.pop();
    }

    public boolean hasDialog() {
        return !dialogs.isEmpty();
    }

    public void update(GameContext context) {
        if (dialogs.isEmpty()) return;

        Dialog dialog = dialogs.peek();

        if (dialog.showCursor() && context.getKeyHandler().isPressed(VK_ENTER)) {
            close();
        }
    }

    private static final int BACKGROUND = 17;
    private static final int TOP_LEFT_BORDER = 18;
    private static final int TOP_RIGHT_BORDER = 19;
    private static final int BOTTOM_LEFT_BORDER = 20;
    private static final int BOTTOM_RIGHT_BORDER = 21;
    private static final int LEFT_BORDER = 22;
    private static final int RIGHT_BORDER = 23;
    private static final int TOP_BORDER = 24;
    private static final int BOTTOM_BORDER = 25;
}

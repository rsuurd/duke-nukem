package duke.modals;

import duke.GameState;
import duke.KeyHandler;

import static java.awt.event.KeyEvent.VK_ENTER;

public class Modal {
    private int x;
    private int y;

    private String text;
    private int closeOperation;

    public Modal(int x, int y, String text, int closeOperation) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.closeOperation = closeOperation;
    }

    public void handleInput(GameState state, KeyHandler handler) {
        if (shouldClose(handler)) {
            state.closeModals();
        }
    }

    private boolean shouldClose(KeyHandler handler) {
        return switch (closeOperation) {
            case EXIT_ON_ANY_KEY -> handler.isAnyKeyPressed();
            case EXIT_ON_ENTER -> handler.isPressed(VK_ENTER);
            default -> false;
        };
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return text;
    }

    public boolean isPrompt() {
        return true;
    }

    public static final int DO_NOTHING = 0;
    public static final int EXIT_ON_ANY_KEY = 1;
    public static final int EXIT_ON_ENTER = 2;
}

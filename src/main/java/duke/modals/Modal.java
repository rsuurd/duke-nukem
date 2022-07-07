package duke.modals;

import duke.GameState;
import duke.KeyHandler;

public class Modal {
    private int x;
    private int y;

    private String text;
    private boolean prompt;

    public Modal(int x, int y, String text, boolean prompt) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.prompt = prompt;
    }

    public void handleInput(GameState state, KeyHandler handler) {
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
        return prompt;
    }
}

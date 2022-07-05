package duke;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    private boolean[] pressedKeys;

    public KeyHandler() {
        pressedKeys = new boolean[0xFFFF];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        handleKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        handleKey(e.getKeyCode(), false);
    }

    private void handleKey(int keyCode, boolean pressed) {
        pressedKeys[keyCode] = pressed;
    }

    public boolean isLeft() {
        return isPressed(KeyEvent.VK_LEFT);
    }

    public boolean isRight() {
        return isPressed(KeyEvent.VK_RIGHT);

    }

    public boolean isJump() {
        return isPressed(KeyEvent.VK_ALT);

    }

    public boolean isFire() {
        return isPressed(KeyEvent.VK_CONTROL);

    }

    public boolean isUsing() {
        return isPressed(KeyEvent.VK_UP);
    }

    public boolean isPressed(int keyCode) {
        return pressedKeys[keyCode];
    }
}

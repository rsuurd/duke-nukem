package duke;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

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

    public void clear() {
        Arrays.fill(pressedKeys, false);
    }

    public boolean isAnyKeyPressed() {
        boolean pressed = false;
        int i = 1; // skip VK_UNDEFINED, that isn't a relevant key to check

        while (!pressed && (i < pressedKeys.length)) {
            pressed = pressedKeys[i];

            i ++;
        }

        return pressed;
    }
}

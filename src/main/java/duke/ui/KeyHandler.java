package duke.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import static java.awt.event.KeyEvent.*;

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

    public boolean isPressed(int keyCode) {
        return pressedKeys[keyCode];
    }

    public Input getInput() {
        return new Input(isPressed(VK_LEFT), isPressed(VK_RIGHT), isPressed(VK_ALT), isPressed(VK_CONTROL), isPressed(VK_UP));
    }

    public void clear() {
        Arrays.fill(pressedKeys, false);
    }

    public boolean isAnyKeyPressed() {
        return getPressedKey() > 0;
    }

    public int getPressedKey() {
        int pressedKey = 0;
        int i = 1; // skip VK_UNDEFINED, that isn't a relevant key to check

        while ((pressedKey == 0) && (i < pressedKeys.length)) {
            if (pressedKeys[i]) {
                pressedKey = i;
            }

            i++;
        }

        return pressedKey;
    }

    public record Input(boolean left, boolean right, boolean jump, boolean fire, boolean using) {
    }
}

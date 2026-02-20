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

    public Input getInput() {
        return new Input(pressedKeys[VK_UP], pressedKeys[VK_DOWN], pressedKeys[VK_LEFT], pressedKeys[VK_RIGHT], pressedKeys[VK_ALT], pressedKeys[VK_CONTROL]);
    }

    public boolean consumeAny() {
        boolean pressed = false;
        int i = 0;

        while (!pressed && i < pressedKeys.length) {
            pressed = pressedKeys[i++];
        }

        if (pressed) {
            Arrays.fill(pressedKeys, false);
        }

        return pressed;
    }

    public boolean consume(int keyCode) {
        boolean pressed = pressedKeys[keyCode];

        pressedKeys[keyCode] = false;

        return pressed;
    }

    public boolean consumeAll(int... keyCodes) {
        boolean allPressed = Arrays.stream(keyCodes).allMatch(keyCode -> pressedKeys[keyCode]);

        if (allPressed) {
            for (int keyCode : keyCodes) {
                pressedKeys[keyCode] = false;
            }
        }

        return allPressed;
    }

    public record Input(boolean up, boolean down, boolean left, boolean right, boolean fire, boolean jump) {
    }
}

package duke;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    @Override
    public void keyPressed(KeyEvent e) {
        handleKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        handleKey(e.getKeyCode(), false);
    }

    private void handleKey(int keyCode, boolean pressed) {
        switch (keyCode) {
            case KeyEvent.VK_UP -> up = pressed;
            case KeyEvent.VK_DOWN -> down = pressed;
            case KeyEvent.VK_LEFT -> left = pressed;
            case KeyEvent.VK_RIGHT -> right = pressed;
        }
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }
}

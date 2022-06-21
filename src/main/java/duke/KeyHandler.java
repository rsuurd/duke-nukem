package duke;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    private boolean down;
    private boolean left;
    private boolean right;

    private boolean jump;
    private boolean fire;
    private boolean using;

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
            case KeyEvent.VK_UP -> using = pressed;
            case KeyEvent.VK_DOWN -> down = pressed;
            case KeyEvent.VK_LEFT -> left = pressed;
            case KeyEvent.VK_RIGHT -> right = pressed;
            case KeyEvent.VK_ALT -> jump = pressed;
            case KeyEvent.VK_CONTROL -> fire = pressed;
        }
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

    public boolean isJump() {
        return jump;
    }

    public boolean isFire() {
        return fire;
    }

    public boolean isUsing() {
        return using;
    }
}

package duke.gameplay;

import duke.ui.KeyHandler;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_UP;

public class Player {
    private int x;
    private int y;

    private int velocityX;
    private int velocityY;

    private boolean grounded = true;

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setVelocity(int velocityX, int velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void processInput(KeyHandler input) {
        if (input.isLeft()) {
            velocityX -= WALK_SPEED;
        }

        if (input.isRight()) {
            velocityX += WALK_SPEED;
        }

        if (input.isPressed(VK_UP)) {
            velocityY -= WALK_SPEED;
        }

        if (input.isPressed(VK_DOWN)) {
            velocityY += WALK_SPEED;
        }
    }

    public void update() {
        x += velocityX;
        y += velocityY;

        this.grounded = velocityY == 0;

        // Friction
        velocityX = 0;
        velocityY = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public boolean isGrounded() {
        return grounded;
    }

    static final int WALK_SPEED = 8;
}

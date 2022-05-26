package duke;

public class Duke {
    private int x;
    private int y;

    private int velocityX;
    private int velocityY;
    private int jumpFramesLeft;

    private boolean touchDown;

    public void accelerate(int deltaX, int deltaY) {
        velocityX += deltaX;
        velocityY += deltaY;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(GameState state) {
        Level level = state.getLevel();

        moveHorizontally(level);
        moveVertically(level);

        applyGravity();
    }

    private void moveHorizontally(Level level) {
        int newPositionX = x + velocityX;

        if (!level.collides(newPositionX, y, 31, 31)) {
            x = newPositionX;
        }

        velocityX = 0;
    }

    private void moveVertically(Level level) {
        boolean collision = false;
        int sign = Integer.signum(velocityY);
        int i = 0;

        while (!collision && (i < Math.abs(velocityY))) {
            int newPositionY = y + sign;

            collision = level.collides(x, newPositionY, 31, 31);

            if (collision) {
                jumpFramesLeft = 0;

                if (velocityY > 0) {
                    touchDown = true;
                }
            } else {
                y = newPositionY;

            }

            i++;
        }
    }

    private void applyGravity() {
        if (jumpFramesLeft > 0) {
            if (jumpFramesLeft > 3) {
                velocityY += 2;
            } else {
                velocityY = 0;
            }

            jumpFramesLeft--;
        } else {
            velocityY = 8;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void jump() {
        if (canJump()) {
            touchDown = false;
            jumpFramesLeft = 8;
            velocityY = -13;
        }
    }

    private boolean canJump() {
        return touchDown;
    }
}

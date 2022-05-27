package duke;

public class Duke {
    private int x;
    private int y;

    private int velocityX;
    private int velocityY;

    private Facing facing;
    private State state;

    private int jumpFramesLeft;
    private int frame;

    public void reset() {
        facing = Facing.LEFT;
        setState(State.STAND);
    }

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

        if (this.state == State.WALK) {
            frame = (frame + 1) % 8;
        }
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

                if (velocityY < 0) {
                    bump();
                } else {
                    land();
                }

                velocityY = 0;
            } else {
                y = newPositionY;
            }

            i++;
        }

        if (velocityY > 0) {
            setState(State.FALL);
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

    public Facing getFacing() {
        return facing;
    }

    public void setState(State state) {
        if (this.state != state) {
            this.state = state;
            frame = 0;
        }
    }

    public State getState() {
        return state;
    }

    public int getFrame() {
        return frame;
    }

    public void move(Facing facing) {
        if (this.facing == facing) {

            switch (facing) {
                case LEFT -> velocityX = -8;
                case RIGHT -> velocityX = 8;
            }

            if (state == State.STAND) {
                setState(State.WALK);
            }
        } else {
            this.facing = facing;
        }
    }

    public void stopMove() {
        if (state == State.WALK) {
            setState(State.STAND);
        }
    }

    public void jump() {
        if (canJump()) {
            jumpFramesLeft = 8;
            velocityY = -13;
            setState(State.JUMP);
        }
    }

    private void bump() {
        setState(State.FALL);
    }

    private void land() {
        if (state == State.FALL || state == State.JUMP) {
            setState(State.STAND);
        }
    }

    private boolean canJump() {
        return (state == State.STAND) || (state == State.WALK);
    }


    enum State {
        STAND, WALK, JUMP, FALL
    }
}

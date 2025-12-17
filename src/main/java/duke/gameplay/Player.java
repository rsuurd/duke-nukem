package duke.gameplay;

import duke.ui.KeyHandler;

public class Player {
    private int x;
    private int y;

    private int velocityX;
    private int velocityY;

    private State state;
    private Facing facing;

    public Player() {
        this(State.STANDING, Facing.LEFT);
    }

    Player(State state, Facing facing) {
        this.facing = facing;
        this.state = state;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setVelocity(int velocityX, int velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void processInput(KeyHandler.Input input) {
        if (input.left()) {
            move(Facing.LEFT);
        }

        if (input.right()) {
            move(Facing.RIGHT);
        }

        if (!input.left() && !input.right()) {
            stopMove();
        }

        if (input.jump()) {
            jump();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return 16;
    }

    public int getHeight() {
        return 32;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public State getState() {
        return state;
    }

    public Facing getFacing() {
        return facing;
    }

    private void move(Facing facing) {
        if (this.facing == facing) {
            velocityX = (facing == Facing.LEFT) ? -SPEED : SPEED;

            if (state == State.STANDING) {
                state = State.WALKING;
            }
        } else {
            this.facing = facing;
        }
    }

    private void stopMove() {
        velocityX = 0;

        if (state == State.WALKING) {
            state = State.STANDING;
        }
    }

    public boolean isGrounded() {
        return state == State.STANDING || state == State.WALKING;
    }

    private void jump() {
        if (isGrounded()) {
            velocityY = JUMP_POWER;
            state = State.JUMPING;
        }
    }

    public void land() {
        velocityY = 0;
        state = State.STANDING;
    }

    public void bump() {
        velocityY = 0;
    }

    public void fall() {
        state = State.FALLING;
    }

    static final int JUMP_POWER = -15; // TODO influenced by boots
    static final int SPEED = 8;

    public enum State {
        STANDING,
        WALKING,
        JUMPING,
        FALLING
    }

    public enum Facing {
        LEFT, RIGHT
    }
}

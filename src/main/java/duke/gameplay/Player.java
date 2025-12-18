package duke.gameplay;

import duke.ui.KeyHandler;

public class Player extends Active implements Collidable {
    private int velocityX;
    private int velocityY;

    private State state;
    private Facing facing;

    private int health;

    public Player() {
        this(State.STANDING, Facing.LEFT);
    }

    Player(State state, Facing facing) {
        super(0, 0, WIDTH, HEIGHT);

        this.facing = facing;
        this.state = state;

        health = 8;
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

    public int getHealth() {
        return health;
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

    @Override
    public void onCollide(Direction direction) {
        if (direction == Direction.DOWN) {
            land();
        } else if (direction == Direction.UP) {
            bump();
        } else {
            velocityX = 0;
        }
    }

    private void land() {
        velocityY = 0;
        state = State.STANDING;
    }

    private void bump() {
        velocityY = 0;
    }

    public void fall() {
        state = State.FALLING;
    }

    private static final int WIDTH = 16;
    private static final int HEIGHT = 32;
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

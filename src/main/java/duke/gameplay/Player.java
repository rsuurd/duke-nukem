package duke.gameplay;

import duke.ui.KeyHandler;

import static duke.level.Level.TILE_SIZE;

public class Player extends Active implements Movable, Collidable, Physics {
    private int velocityX;
    private int velocityY;

    private State state;
    private Facing facing;
    private int hangTimeLeft;
    private boolean moving;
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

    public void processInput(KeyHandler.Input input) {
        if (input.left()) {
            move(Facing.LEFT);
        }

        if (input.right()) {
            move(Facing.RIGHT);
        }

        moving = input.left() || input.right();

        if (input.jump()) {
            jump();
        }
    }

    public void update() {
        applyFriction();
        updateJump();
    }

    private void updateJump() {
        if (getVelocityY() < 0) return;

        if (state == State.JUMPING) {
            if (--hangTimeLeft > 0) {
                velocityY = 0;
            } else {
                state = State.FALLING;
            }
        }
    }

    // maybe move this over to Physics
    private void applyFriction() {
        if (!moving) {
            setVelocityX(0);

            if (state == State.WALKING) {
                state = State.STANDING;
            }
        }
    }

    @Override
    public int getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    @Override
    public int getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
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

    public boolean isGrounded() {
        return state == State.STANDING || state == State.WALKING;
    }

    private void jump() {
        if (isGrounded()) {
            setVelocityY(JUMP_POWER);
            state = State.JUMPING;
            hangTimeLeft = HANG_TIME;
        }
    }

    @Override
    public void onCollision(Direction direction) {
        if (direction == Direction.DOWN) {
            land();
        } else if (direction == Direction.UP) {
            bump();
        } else {
            setVelocityX(0);
        }
    }

    private void land() {
        setVelocityY(0);
        state = moving ? State.WALKING : State.STANDING;
        hangTimeLeft = 0;
    }

    private void bump() {
        setVelocityY(0);
        hangTimeLeft = 0;
    }

    public void fall() {
        if (state != State.JUMPING) {
            state = State.FALLING;
        }
    }

    @Override
    public int getVerticalAcceleration() {
        return switch (state) {
            case STANDING, WALKING -> 0;
            case JUMPING -> isHanging() ? 0 : GRAVITY;
            case FALLING -> SPEED;
        };
    }

    @Override
    public int getTerminalVelocity() {
        return TILE_SIZE;
    }

    private boolean isHanging() {
        return state == State.JUMPING && hangTimeLeft > 0 && getVelocityY() == 0;
    }

    private static final int WIDTH = 16;
    private static final int HEIGHT = 32;
    static final int JUMP_POWER = -15; // TODO influenced by boots
    static final int HANG_TIME = 4;
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

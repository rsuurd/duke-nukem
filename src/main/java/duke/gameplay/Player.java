package duke.gameplay;

import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.resources.AssetManager;
import duke.ui.KeyHandler;

public class Player extends Active implements Movable, Collidable, Physics, Updatable, SpriteRenderable {
    private State state;
    private Facing facing;
    private boolean jumpReady;
    private int hangTimeLeft;
    private boolean moving;
    private boolean firing;
    private int health;

    private SpriteDescriptor spriteDescriptor;
    private Animation animation;

    public Player() {
        this(State.STANDING, Facing.RIGHT);
    }

    Player(State state, Facing facing) {
        super(0, 0, WIDTH, HEIGHT);

        this.facing = facing;
        this.state = state;

        health = 8;
        jumpReady = true;

        spriteDescriptor = (facing == Facing.LEFT) ? STANDING_LEFT : STANDING_RIGHT;
        animation = new Animation(WALKING_LEFT);
    }

    public void processInput(KeyHandler.Input input) {
        if (input.left()) {
            move(Facing.LEFT);
        }

        if (input.right()) {
            move(Facing.RIGHT);
        }

        moving = input.left() || input.right();
        firing = input.fire();

        if (input.jump()) {
            jump();
        } else {
            jumpReady = isGrounded();
        }
    }

    @Override
    public void update(GameplayContext context) {
        applyFriction();
        updateJump();
        updateAnimation();

        // context.collision().resolve(...);
        // if shoot pressed and can shoot, spawn bolt
    }

    public boolean isFiring() {
        return firing;
    }

    private void updateJump() {
        if (getVelocityY() < 0) return;

        if (state == State.JUMPING) {
            if (--hangTimeLeft > 0) {
                setVelocityY(0);
            } else {
                state = State.FALLING;
            }
        }
    }

    private void applyFriction() {
        if (!moving) {
            setVelocityX(0);

            if (state == State.WALKING) {
                state = State.STANDING;
            }
        }
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
            setVelocityX((facing == Facing.LEFT) ? -SPEED : SPEED);

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
        if (jumpReady && isGrounded()) {
            setVelocityY(JUMP_POWER);
            state = State.JUMPING;
            jumpReady = false;
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

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return spriteDescriptor;
    }

    private void updateAnimation() {
        // TODO maybe create a map or array with some indexing instead of this horrible switch
        switch (state) {
            case STANDING -> {
                if (firing) {
                    spriteDescriptor = facing == Facing.LEFT ? SHOOT_LEFT : SHOOT_RIGHT;
                } else {
                    spriteDescriptor = (facing == Facing.LEFT) ? STANDING_LEFT : STANDING_RIGHT;
                }
            }
            case JUMPING -> spriteDescriptor = (facing == Facing.LEFT) ? JUMPING_LEFT : JUMPING_RIGHT;
            case FALLING -> spriteDescriptor = (facing == Facing.LEFT) ? FALLING_LEFT : FALLING_RIGHT;
            case WALKING -> {
                animation.tick();
                animation.setAnimation(facing == Facing.LEFT ? WALKING_LEFT : WALKING_RIGHT);
                spriteDescriptor = animation.getSpriteDescriptor();
            }
        }
    }

    private static SpriteDescriptor BASE_DESCRIPTOR = new SpriteDescriptor(AssetManager::getMan, 0, -8, 0, 2, 2);

    private static SpriteDescriptor STANDING_LEFT = BASE_DESCRIPTOR.withBaseIndex(50);
    private static SpriteDescriptor STANDING_RIGHT = BASE_DESCRIPTOR.withBaseIndex(54);
    private static AnimationDescriptor WALKING_LEFT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(0), 4, 2);
    private static AnimationDescriptor WALKING_RIGHT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(16), 4, 2);
    private static SpriteDescriptor JUMPING_LEFT = BASE_DESCRIPTOR.withBaseIndex(32);
    private static SpriteDescriptor JUMPING_RIGHT = BASE_DESCRIPTOR.withBaseIndex(36);
    private static SpriteDescriptor FALLING_LEFT = BASE_DESCRIPTOR.withBaseIndex(40);
    private static SpriteDescriptor FALLING_RIGHT = BASE_DESCRIPTOR.withBaseIndex(44);
    private static SpriteDescriptor SHOOT_LEFT = BASE_DESCRIPTOR.withBaseIndex(12);
    private static SpriteDescriptor SHOOT_RIGHT = BASE_DESCRIPTOR.withBaseIndex(28);
}

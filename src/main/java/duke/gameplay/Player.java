package duke.gameplay;

import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;
import duke.ui.KeyHandler;

import java.util.List;

public class Player extends Active implements Movable, Collidable, Physics, Updatable, SpriteRenderable {
    private State state;
    private Facing facing;
    private boolean jumpReady;
    private int hangTimeLeft;
    private boolean moving;
    private boolean firing;

    private int health;

    public Player() {
        this(State.STANDING, Facing.LEFT);
    }

    Player(State state, Facing facing) {
        super(0, 0, WIDTH, HEIGHT);

        this.facing = facing;
        this.state = state;

        health = 8;
        jumpReady = true;
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
    }

    private void updateAnimation() {
        animation.tick();

        // swap animation based on state and facing, should probably make something more sophisticated later
        if (firing && state == State.STANDING) {
            animation.setAnimation(facing == Facing.LEFT ? SHOOT_LEFT : SHOOT_RIGHT);
        } else {
            int animationIndex = (state.ordinal() * 2) + facing.ordinal();
            animation.setAnimation(ANIMATIONS.get(animationIndex));
        }
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

    public enum Facing {
        LEFT, RIGHT
    }

    private Animation animation = new Animation(ANIMATIONS.getFirst());

    public Animation getAnimation() {
        return animation;
    }

    private static SpriteDescriptor BASE_DESCRIPTOR = new SpriteDescriptor(AssetManager::getMan, 0, -8, 0, 2, 2);

    private static List<AnimationDescriptor> ANIMATIONS = List.of(
        // standing left/right
        new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(50), 1, 1),
        new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(54), 1, 1),
        // walking left/right
        new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(0), 4, 2),
        new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(16), 4, 2),
        // jumping left/right
        new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(32), 1, 1),
        new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(36), 1, 1),
        // falling left/right
        new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(40), 1, 1),
        new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(44), 1, 1)
    );

    //shooting left/right
    private static AnimationDescriptor SHOOT_LEFT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(12), 1, 1);
    private static AnimationDescriptor SHOOT_RIGHT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(28), 1, 1);
}

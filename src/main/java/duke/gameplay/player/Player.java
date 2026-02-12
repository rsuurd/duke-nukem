package duke.gameplay.player;

import duke.gameplay.*;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Flags;
import duke.ui.KeyHandler;

import java.util.Random;

import static duke.level.Level.TILE_SIZE;

public class Player extends Active implements Movable, Collidable, Physics, Updatable, SpriteRenderable {
    private State state;
    private Facing facing;

    private boolean jumped;
    private boolean bumped;
    private boolean landed;

    private boolean controllable;
    private boolean jumpReady;
    private int jumpTicks;
    private int fallTicks;
    private boolean moving;
    private boolean using;
    private boolean flipping;

    private Random random;
    private Weapon weapon;
    private PlayerHealth health;
    private Inventory inventory;
    private PlayerAnimator animator;
    private PlayerFeedback feedback;

    // Jump / Fall / Walk / Stand handlers?
    private ClingHandler clingHandler;

    public Player() {
        this(State.STANDING, Facing.RIGHT, new Random(), new Weapon(), new PlayerHealth(), new Inventory(), new PlayerAnimator(), new PlayerFeedback(), new ClingHandler());
    }

    Player(State state, Facing facing, Random random, Weapon weapon, PlayerHealth health, Inventory inventory, PlayerAnimator animator, PlayerFeedback feedback, ClingHandler clingHandler) {
        super(0, 0, WIDTH, HEIGHT);

        this.facing = facing;
        this.state = state;

        enableControls();
        jumpReady = true;

        this.random = random;
        this.weapon = weapon;
        this.health = health;
        this.inventory = inventory;
        this.animator = animator;
        this.feedback = feedback;
        this.clingHandler = clingHandler;

        reset();
    }

    public void processInput(KeyHandler.Input input) {
        if (!controllable) return;

        if (input.left()) {
            move(Facing.LEFT);
        }

        if (input.right()) {
            move(Facing.RIGHT);
        }

        moving = input.left() || input.right();

        weapon.setTriggered(input.fire());
        using = input.using();

        if (input.jump()) {
            jump();
        } else {
            jumpReady = isGrounded();
        }
    }

    @Override
    public void update(GameplayContext context) {
        applyFriction();

        // TODO unify these two in a separate component
        updateFall();
        updateJump();

        health.update(context);
        clingHandler.update(context); // maybe check if state == clinging?
    }

    private void reset() {
        jumped = false;
        landed = false;
        bumped = false;

        health.resetDamageTaken();
    }

    public void postMovement(GameplayContext context) {
        weapon.fire(context);
        feedback.provideFeedback(context, this, jumped, bumped, landed, health.isDamageTaken());
        animator.animate(this);

        reset();
    }

    @Override
    public boolean isVisible() {
        return !health.isInvulnerable() || health.getInvulnerability() % 2 == 1;
    }

    private void updateJump() {
        if (state == State.JUMPING) {
            jumped = jumpTicks == JUMP_TICKS;

            if (health.isDamageTaken()) {
                state = State.FALLING;
                fallTicks = FALL_TICKS;
            } else if (jumpTicks > 0) {
                jumpTicks--;
            } else {
                state = State.FALLING;
                fallTicks = SLOW_FALL_TICKS;
            }
        }
    }

    private void applyFriction() {
        if (!moving) {
            // TODO -8 / + 8 instead of hard 0
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

    public Weapon getWeapon() {
        return weapon;
    }

    public PlayerHealth getHealth() {
        return health;
    }

    public Inventory getInventory() {
        return inventory;
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
            boolean hasBoots = inventory.isEquippedWith(Inventory.Equipment.BOOTS);

            setVelocityY(hasBoots ? HIGH_JUMP_POWER : JUMP_POWER);
            state = State.JUMPING;
            jumpReady = false;
            jumpTicks = JUMP_TICKS;
            jumped = true;
            flipping = hasBoots && getVelocityX() != 0 && random.nextBoolean();
        }
    }

    @Override
    public void onCollision(Direction direction, int flags) {
        if (direction == Direction.DOWN) {
            land();
        } else if (direction == Direction.UP) {
            bump();
            clingHandler.onBump(this, flags);
        } else {
            setVelocityX(0);
        }
    }

    private void land() {
        if (!(state == State.FALLING || state == State.JUMPING)) return;

        setVelocityY(0);
        landed = true;
        state = moving ? State.WALKING : State.STANDING;
        jumpTicks = 0;
        fallTicks = 0;
        flipping = false;
    }

    void cling() {
        setVelocityY(0);
        jumpTicks = 0;
        state = State.CLINGING;
        flipping = false;
    }

    void releaseCling() {
        state = State.STANDING;
        fall();
    }

    private void bump() {
        setVelocityY(0);
        jumpTicks = 0;
        // +1 because fall is updated separately and we will be off by 1
        // we can fix this by unifying jump and fall into a single update call
        fallTicks = FALL_TICKS + 1;
        state = State.FALLING;
        bumped = true;
    }

    public void fall() {
        if (state != State.JUMPING && state != State.FALLING && state != State.CLINGING) {
            state = State.FALLING;
            fallTicks = FALL_TICKS;
        }
    }

    private void updateFall() {
        if (state == State.FALLING && fallTicks > 0) {
            fallTicks--;
        }
    }

    @Override
    public int getVerticalAcceleration() {
        return switch (state) {
            case STANDING, WALKING, CLINGING -> 0;
            case JUMPING -> getVerticalAccelerationWhileJumping();
            case FALLING -> SPEED;
        };
    }

    private int getVerticalAccelerationWhileJumping() {
        if (jumpTicks <= 0 || getVelocityY() >= 0) {
            return 0;
        }

        boolean hasBoots = inventory.isEquippedWith(Inventory.Equipment.BOOTS);
        if (!hasBoots && getVelocityY() >= -3) {
            return -getVelocityY();
        }

        // normal rising gravity (clamped so it never goes past 0)
        return Math.min(GRAVITY, -getVelocityY());
    }

    @Override
    public int getTerminalVelocity() {
        return fallTicks > 0 ? SPEED : TILE_SIZE;
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animator.getSpriteDescriptor();
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isUsing() {
        return using;
    }

    public boolean isFlipping() {
        return flipping;
    }

    public void disableControls() {
        controllable = false;
        moving = false;
        weapon.setTriggered(false);
        using = false;
        setVelocityX(0);
        setVelocityY(0);
    }

    public void enableControls() {
        controllable = true;
    }

    public boolean isControllable() {
        return controllable;
    }

    public String debug() {
        return """
                position: %d, %d
                velocity: %d, %d
                %s %s
                jmp: %d, fall: %d, vAcc: %d
                """.formatted(getX(), getY(), getVelocityX(), getVelocityY(), getState(), getFacing(), jumpTicks, fallTicks, getVerticalAcceleration());
    }

    private static final int WIDTH = 16;
    private static final int HEIGHT = 32;

    static final int JUMP_POWER = -15;
    static final int HIGH_JUMP_POWER = JUMP_POWER - 2;
    static final int JUMP_TICKS = 9;

    static final int SLOW_FALL_TICKS = 8;
    static final int FALL_TICKS = 2;

    static final int SPEED = 8;
}

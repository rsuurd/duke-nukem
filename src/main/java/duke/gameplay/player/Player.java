package duke.gameplay.player;

import duke.gameplay.*;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.ui.KeyHandler;

import java.util.Random;

public class Player extends Active implements Movable, Collidable, Physics, Updatable, SpriteRenderable {
    private KeyHandler.Input input;

    private State state;
    private Facing facing;

    private boolean jumped;
    private boolean bumped;
    private boolean landed;

    private boolean controllable;
    private boolean moving;
    private boolean flipping;

    private Weapon weapon;
    private PlayerHealth health;
    private Inventory inventory;

    private MovementHandler movementHandler;
    private JumpHandler jumpHandler;
    private FallHandler fallHandler;
    private GrapplingHooks grapplingHooks;
    private PullUpHandler pullUpHandler;

    private PlayerAnimator animator;
    private PlayerFeedback feedback;

    public Player() {
        this(NONE, State.STANDING, Facing.RIGHT, new Weapon(), new PlayerHealth(), new Inventory(), new MovementHandler(), new JumpHandler(new Random()), new FallHandler(), new GrapplingHooks(), new PullUpHandler(), new PlayerAnimator(), new PlayerFeedback());
    }

    Player(KeyHandler.Input input, State state, Facing facing, Weapon weapon, PlayerHealth health, Inventory inventory, MovementHandler movementHandler, JumpHandler jumpHandler, FallHandler fallHandler, GrapplingHooks grapplingHooks, PullUpHandler pullUpHandler, PlayerAnimator animator, PlayerFeedback feedback) {
        super(0, 0, WIDTH, HEIGHT);

        this.input = input;
        this.facing = facing;
        this.state = state;

        enableControls();

        this.weapon = weapon;
        this.health = health;
        this.inventory = inventory;

        this.movementHandler = movementHandler;
        this.jumpHandler = jumpHandler;
        this.fallHandler = fallHandler;
        this.grapplingHooks = grapplingHooks;
        this.pullUpHandler = pullUpHandler;

        this.animator = animator;
        this.feedback = feedback;

        reset();
    }

    public void processInput(KeyHandler.Input input) {
        if (!controllable) return;

        this.input = input;
        moving = input.left() || input.right();

        movementHandler.handleInput(this, input);
        weapon.setTriggered(input.fire());
        jumpHandler.handleInput(this, input);
    }

    @Override
    public void update(GameplayContext context) {
        health.update(context);
        fallHandler.update(this);
        jumpHandler.update(this);
        pullUpHandler.update(this);
    }

    private void reset() {
        jumped = false;
        landed = false;
        bumped = false;

        health.resetDamageTaken();
    }

    public void postMovement(GameplayContext context) {
        weapon.fire(context);
        grapplingHooks.update(context, input);
        feedback.provideFeedback(context, this, jumped, bumped, landed, health.isDamageTaken());
        animator.animate(this);

        reset();
    }

    @Override
    public boolean isVisible() {
        return !health.isInvulnerable() || health.getInvulnerability() % 2 == 1;
    }

    public State getState() {
        return state;
    }

    public Facing getFacing() {
        return facing;
    }

    public void setFacing(Facing facing) {
        this.facing = facing;
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

    public boolean isGrounded() {
        return state == State.STANDING || state == State.WALKING;
    }

    @Override
    public void onCollision(Direction direction, int flags) {
        if (direction == Direction.DOWN) {
            land();
        } else if (direction == Direction.UP) {
            bump();
        } else {
            setVelocityX(0);
        }
    }

    void walk() {
        if (state == State.STANDING) {
            state = State.WALKING;
        }
    }

    void stand() {
        state = State.STANDING;
    }

    void jump(boolean flipping) {
        jumped = true;
        state = State.JUMPING;
        this.flipping = flipping;
    }

    void fastFall() {
        state = State.FALLING;
        fallHandler.fall();
    }

    void slowFall() {
        state = State.FALLING;
        fallHandler.slowFall();
    }

    private void land() {
        if (!(state == State.FALLING || state == State.JUMPING)) return;

        setVelocityY(0);
        landed = true;
        state = moving ? State.WALKING : State.STANDING;
        flipping = false;
    }

    void cling() {
        setVelocityY(0);
        state = State.CLINGING;
        flipping = false;
    }

    void releaseCling() {
        state = State.STANDING;
        fastFall();
    }

    void pullUp() {
        disableControls();
        pullUpHandler.beginPullUp();
        state = State.PULLING_UP;
    }

    void pullUpComplete() {
        state = State.STANDING;
        enableControls();
    }

    private void bump() {
        setVelocityY(0);
        fastFall();
        bumped = true;
    }

    @Override
    public void fall() { // called by collision system when no ground below
        if (state == State.STANDING || state == State.WALKING) {
            fastFall();
        }
    }

    @Override
    public int getVerticalAcceleration() {
        return switch (state) {
            case JUMPING -> jumpHandler.getVerticalAcceleration(this);
            case FALLING -> fallHandler.getVerticalAcceleration();
            default -> 0;
        };
    }

    @Override
    public int getTerminalVelocity() {
        return fallHandler.getTerminalVelocity();
    }

    @Override
    public boolean isCollisionEnabled() {
        return state != State.PULLING_UP;
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animator.getSpriteDescriptor();
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isUsing() {
        return input.up();
    }

    public boolean isFlipping() {
        return flipping;
    }

    public void disableControls() {
        controllable = false;
        moving = false;
        weapon.setTriggered(false);
        setVelocityX(0);
        setVelocityY(0);
    }

    public void enableControls() {
        controllable = true;
    }

    public boolean isControllable() {
        return controllable;
    }

    private static final int WIDTH = 16;
    private static final int HEIGHT = 32;

    static final int SPEED = 8;
    static final int MAX_SPEED = 8;

    private static final KeyHandler.Input NONE = new KeyHandler.Input(false, false, false, false, false, false);
}

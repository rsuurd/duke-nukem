package duke.gameplay.player;

import duke.gameplay.*;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.ui.KeyHandler;

import static duke.sfx.Sfx.*;

public class Player extends Active implements Movable, Collidable, Physics, Updatable, SpriteRenderable {
    private State state;
    private Facing facing;

    private boolean jumped;
    private boolean bumped;
    private boolean landed;

    private boolean jumpReady;
    private int hangTimeLeft;
    private boolean moving;
    private boolean using;

    private Weapon weapon;
    private Health health;
    private Inventory inventory;
    private PlayerAnimator animator;

    public Player() {
        this(State.STANDING, Facing.RIGHT, new Weapon(), new Health(), new Inventory(), new PlayerAnimator());
    }

    Player(State state, Facing facing, Weapon weapon, Health health, Inventory inventory, PlayerAnimator animator) {
        super(0, 0, WIDTH, HEIGHT);

        this.facing = facing;
        this.state = state;

        jumpReady = true;

        this.weapon = weapon;
        this.health = health;
        this.inventory = inventory;
        this.animator = animator;

        reset();
    }

    public void processInput(KeyHandler.Input input) {
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
        reset();

        applyFriction();
        updateJump();

        health.update(context);
    }

    private void reset() {
        jumped = false;
        landed = false;
        bumped = false;
    }

    public void postMovement(GameplayContext context) {
        checkDamage(context);

        weapon.fire(context);

        if (jumped) {
            context.getSoundManager().play(PLAYER_JUMP);
        }
        if (bumped) {
            context.getSoundManager().play(HIT_HEAD);
        }
        if (landed) {
            context.getActiveManager().spawn(EffectsFactory.createDust(this));
            context.getSoundManager().play(PLAYER_LAND);
        }

        animator.animate(this);
    }

    private void checkDamage(GameplayContext context) {
        if (health.isInvulnerable()) return;

        for (Active active : context.getActiveManager().getActives()) {
            if (active instanceof Damaging damaging && intersects(active)) {
                health.takeDamage(damaging);
                context.getSoundManager().play(PLAYER_HIT);
                break;
            }
        }
    }

    @Override
    public boolean isVisible() {
        return !health.isInvulnerable() || health.getInvulnerability() % 2 == 0;
    }

    private void updateJump() {
        jumped = getVelocityY() == JUMP_POWER;
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

    public Weapon getWeapon() {
        return weapon;
    }

    public Health getHealth() {
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
        if (state == State.FALLING || state == State.JUMPING) {
            landed = true;
        }
        state = moving ? State.WALKING : State.STANDING;
        hangTimeLeft = 0;
    }

    private void bump() {
        setVelocityY(0);
        hangTimeLeft = 0;
        bumped = true;
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

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animator.getSpriteDescriptor();
    }

    public boolean isUsing() {
        return using;
    }

    public enum State {
        STANDING,
        WALKING,
        JUMPING,
        FALLING
    }

    private static final int WIDTH = 16;
    private static final int HEIGHT = 32;

    static final int JUMP_POWER = -15; // TODO influenced by boots
    static final int HANG_TIME = 4;
    static final int SPEED = 8;
}

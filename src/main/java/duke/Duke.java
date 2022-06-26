package duke;

import duke.active.Active;
import duke.effects.Effect;

import static duke.Gfx.TILE_SIZE;

public class Duke extends Active {
    public static final int MAX_HEALTH = 8;

    private Facing facing;
    private State state;

    private int jumpFramesLeft;
    private int frame;

    private int health;
    private int firePower = 1;

    private int invincibility;
    private boolean triggerPulled;
    private boolean using;

    public Duke() {
        super(0, 0);

        height = 31;
    }

    public void reset(Level level) {
        facing = Facing.LEFT;
        setState(State.STAND);
        moveTo(level.getPlayerStartX(), level.getPlayerStartY());

        health = MAX_HEALTH;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(GameState state) {
        Level level = state.getLevel();

        if (invincibility > 0) {
            invincibility--;
        }

        moveHorizontally(level);
        moveVertically(state);

        if (velocityY > 0) {
            setState(State.FALL);
        }

        applyGravity();

        if (this.state == State.WALK) {
            frame = (frame + 1) % 8;
        }

        if (triggerPulled && (state.getBolts().size() < firePower)) {
            state.getBolts().add(new Bolt(x, y + 12, facing));
        }
    }

    public void render(Renderer renderer, Assets assets) {
        int tileIndex = switch (state) {
            case STAND -> 50;
            case WALK -> 0;
            case JUMP -> 32;
            case FALL -> 40;
        };

        tileIndex += (facing == Facing.LEFT) ? 0 : (state == Duke.State.WALK) ? 16 : 4;
        tileIndex += ((frame / 2) * 4);

        // override with shoot tile
        if (triggerPulled && (state == State.STAND)) {
            tileIndex = (facing == Facing.LEFT) ? 12 : 28;
        }

        // override with xray 182
        if (invincibility > 28) {
            tileIndex = 182 + ((facing == Facing.LEFT) ? 0 : 4);
        }

        if (((invincibility % 2) == 0)) {
            int screenX = x - 8;

            renderer.drawTile(assets.getMan(tileIndex), screenX, y);
            renderer.drawTile(assets.getMan(tileIndex + 1), screenX + TILE_SIZE, y);
            renderer.drawTile(assets.getMan(tileIndex + 2), screenX, y + TILE_SIZE);
            renderer.drawTile(assets.getMan(tileIndex + 3), screenX + TILE_SIZE, y + TILE_SIZE);
        }
    }

    @Override
    protected void applyGravity() {
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

    public void setState(State state) {
        if (this.state != state) {
            this.state = state;
            frame = 0;
        }
    }

    public State getState() {
        return state;
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

    @Override
    protected void bump(GameState state) {
        jumpFramesLeft = 0;

        setState(State.FALL);
    }

    @Override
    protected void land(GameState state) {
        if (this.state == State.FALL || this.state == State.JUMP) {
            setState(State.STAND);

            state.addEffect(new Effect.Dustcloud(x, y + TILE_SIZE));
        }
    }

    private boolean canJump() {
        return (state == State.STAND) || (state == State.WALK);
    }

    public int getHealth() {
        return health;
    }

    public int getFirePower() {
        return firePower;
    }

    public void fire(boolean triggerPulled) {
        this.triggerPulled = triggerPulled;
    }

    public void setUsing(boolean using) {
        this.using = using;
    }

    public boolean isUsing() {
        return using;
    }

    public void hurt() {
        if (!isInvincible()) {
            invincibility = 32;

//            health--;
        }
    }

    public void increaseHealth(int amount) {
        health = Math.min(MAX_HEALTH, health + amount);
    }

    public boolean isInvincible() {
        return invincibility > 0;
    }

    enum State {
        STAND, WALK, JUMP, FALL
    }
}

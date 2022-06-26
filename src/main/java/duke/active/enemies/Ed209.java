package duke.active.enemies;

import duke.*;
import duke.active.Active;
import duke.effects.Debris;
import duke.effects.Effect;

import static duke.Gfx.TILE_SIZE;

public class Ed209 extends Active {
    private static final int SHOOT = 10;
    private static final int JUMP = 20;

    private Facing facing;

    private int timer;
    private int jumpFramesLeft;

    private State state;
    private int health;

    public Ed209(int x, int y) {
        super(x, y);

        width = (2 * TILE_SIZE) - 1;
        height = (2 * TILE_SIZE) - 1;

        this.facing = Facing.LEFT;
        this.state = State.STAND;

        health = 3;
    }

    @Override
    public void update(GameState state) {
        Duke duke = state.getDuke();

        if (this.state != State.JUMP) {
            facing = ((x + 8) < duke.getX()) ? Facing.RIGHT : Facing.LEFT;
        }

        if (this.state == State.SHOOT) {
            this.state = State.STAND;

            int shotX = (facing == Facing.LEFT) ? (x - TILE_SIZE) : (x + width);
            state.spawn(new Shot(shotX, y + 10, facing));
        }

        if (timer == SHOOT) {
            this.state = State.SHOOT;
        } else if (timer == JUMP) {
            this.state = State.JUMP;
            jumpFramesLeft = 9;
            velocityY = -32;
        }

        if (this.state == State.JUMP) {
            velocityX = (facing == Facing.LEFT) ? -4 : 4;
        }

        super.update(state);

        if (duke.collidesWith(this)) {
            duke.hurt();
        }

        if ((timer % 4) == 0) {
            if (health < 3) {
                state.addEffect(new Effect.Smoke(x, y - 4));
            }

            if (health < 2) {
                state.addEffect(new Effect.Smoke(x + TILE_SIZE, y - 4));
            }
        }

        timer ++;
    }

    @Override
    protected void applyGravity() {
        if (velocityY > 0) {
            this.state = State.FALL;
        }

        if (jumpFramesLeft > 0) {
            if (jumpFramesLeft > 2) {
                velocityY /= 2;
            } else {
                velocityY++;
            }

            jumpFramesLeft--;
        } else {
            velocityY = TILE_SIZE;
        }
    }

    @Override
    protected void land(GameState state) {
        if ((this.state == State.JUMP) || (this.state == State.FALL)) {
            this.state = State.STAND;

            timer = 0;
        }
    }

    @Override
    public boolean canBeShot() {
        return true;
    }

    @Override
    public void hit(GameState state) {
        health --;

        if (health == 0) {
            state.increaseScore(2000);

            state.addEffect(new Effect.Sparks(x + 8, y));
            state.addEffect(new Effect.Sparks(x + 8, y + TILE_SIZE));
            state.addEffect(new Effect.Sparks(x + TILE_SIZE, y));
            state.addEffect(new Effect.Sparks(x + TILE_SIZE, y + 8));

            Effect.Particle.createParticles(state, x + 8, y - TILE_SIZE);
            Effect.Particle.createParticles(state, x, y);
            Effect.Particle.createParticles(state, x + TILE_SIZE, y + TILE_SIZE);

            if ((timer % 2) == 0) {
                state.addEffect(new Debris(x, y ));
            }
            active = false;
        }
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        if ((state == State.JUMP) || (state == State.FALL)) {
            int tileIndex = (facing == Facing.LEFT) ? 16 : 28;

            renderer.drawTile(assets.getAnim(tileIndex), x, y - TILE_SIZE);
            renderer.drawTile(assets.getAnim(tileIndex + 1), x + TILE_SIZE, y - TILE_SIZE);
            renderer.drawTile(assets.getAnim(tileIndex + 2), x, y);
            renderer.drawTile(assets.getAnim(tileIndex + 3), x + TILE_SIZE, y);
            renderer.drawTile(assets.getAnim(tileIndex + 4), x, y + TILE_SIZE);
            renderer.drawTile(assets.getAnim(tileIndex + 5), x + TILE_SIZE, y + TILE_SIZE);
        } else {
            int tileIndex = (facing == Facing.LEFT) ? 10 : 22;

            renderer.drawTile(assets.getAnim(tileIndex), x, y);
            renderer.drawTile(assets.getAnim(tileIndex + 1), x + TILE_SIZE, y);

            int legsIndex = tileIndex + ((state == State.SHOOT) ? 4 : 2);
            renderer.drawTile(assets.getAnim(legsIndex), x, y + TILE_SIZE);
            renderer.drawTile(assets.getAnim(legsIndex + 1), x + TILE_SIZE, y + TILE_SIZE);
        }
    }

    enum State {
        STAND, SHOOT, JUMP, FALL
    }
}

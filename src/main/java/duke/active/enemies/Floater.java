package duke.active.enemies;

import duke.*;
import duke.active.Active;
import duke.effects.Effect;

import static duke.Gfx.TILE_SIZE;

public class Floater extends Active {
    private static final int SHOT_TIMER = 24;

    private int frame;
    private int facing;

    private int shotTimer;

    private boolean crashing;
    private int crashVelocityX;

    public Floater(int x, int y) {
        super(x, y);

        frame = 0;
        shotTimer = SHOT_TIMER;
    }

    @Override
    public void update(GameState state) {
        Duke duke = state.getDuke();

        if (crashing) {
            state.addEffect(new Effect.Smoke(x, y));
            velocityX = crashVelocityX;
            velocityY += 2;
        } else {
            if (frame == 0) {
                int direction = Integer.signum(duke.getX() / TILE_SIZE - x / TILE_SIZE);

                facing = Math.max(0, Math.min(5, facing + direction));

                if (!isTurning()) {
                    velocityX = direction * 8;
                }
            }

            shotTimer--;

            if (isTurning()) {
                shotTimer = SHOT_TIMER;
            } else if (shotTimer <= 0) {
                Facing facing = (this.facing == 0) ? Facing.LEFT : Facing.RIGHT;

                state.spawn(new Shot(x + ((facing == Facing.LEFT) ? -TILE_SIZE : TILE_SIZE), y, facing));
                shotTimer = SHOT_TIMER;
            }

            if (duke.collidesWith(this)) {
                int distance = y - duke.getY();

                if (distance < TILE_SIZE) {
                    duke.hurt();
                }

                crash(state);
            } else {
                velocityY = Integer.signum(duke.getY() + TILE_SIZE - y);
            }

            frame = (frame + 1) % 4;
        }

        super.update(state);
    }

    @Override
    public boolean canBeShot() {
        return !crashing;
    }

    @Override
    public void hit(GameState state) {
        if (!crashing) {
            crash(state);
        }
    }

    private void crash(GameState state) {
        state.increaseScore(200);
        crashing = true;

        state.addEffect(new Effect.Sparks(x, y));
        velocityY = -TILE_SIZE;
        crashVelocityX = Integer.signum(x + 8 - state.getDuke().getX() + 8) * 4;
    }

    @Override
    protected void bump(GameState state) {
        destroy(state);
    }

    @Override
    protected void land(GameState state) {
        destroy(state);
    }

    @Override
    protected void hitWall(GameState state) {
        destroy(state);
    }

    private void destroy(GameState state) {
        if (crashing) {
            active = false;
            state.addEffect(new Effect.Sparks(x, y));
            Effect.Particle.createParticles(state, x, y);
        }
    }

    @Override
    protected void applyGravity() {}

    private boolean isTurning() {
        return ((facing > 0) && (facing < 5));
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getAnim(facing), x , y);
        renderer.drawTile(assets.getAnim(6 + frame), x , y + TILE_SIZE);
    }
}

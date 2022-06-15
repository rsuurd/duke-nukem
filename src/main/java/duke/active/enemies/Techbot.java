package duke.active.enemies;

import duke.*;
import duke.active.Active;

import static duke.Gfx.TILE_SIZE;

public class Techbot extends Active {
    private static final int SPEED = 8;
    private static final int SIZE = TILE_SIZE - 1;
    private static final int DEATH_TIMER = 7;

    private Facing facing;

    private int frame;
    private int exploding;

    public Techbot(int x, int y) {
        super(x, y);

        facing = Facing.RIGHT;
        frame = 0;
        exploding = -1;
    }

    @Override
    public void update(GameState state) {
        super.update(state);

        if (isAlive()) {
            if ((frame % 2) == 0) {
                tryMove(state.getLevel());
            }

            if (state.getDuke().collidesWith(this)) {
                state.getDuke().hurt();
            }

            checkHit(state);
        } else {
            if (exploding == DEATH_TIMER) {
                state.increaseScore(100);

                active = false;
            }
        }
    }

    private boolean isAlive() {
        return exploding < 0;
    }

    private void tryMove(Level level) {
        int destinationX = x + ((facing == Facing.LEFT) ? -SPEED : SPEED);

        boolean free = !level.collides(destinationX, y, SIZE, SIZE);
        boolean solidGround = level.collides(destinationX, y + 8, SIZE, SIZE);

        if (free && solidGround) {
            velocityX = destinationX - x;
        } else {
            facing = (facing == Facing.LEFT) ? Facing.RIGHT : Facing.LEFT;
        }
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        if (isAlive()) {
            renderer.drawTile(assets.getAnim(82 + frame), x, y);

            frame = (frame + 1) % 3;
        } else {
            renderer.drawTile(assets.getAnim(85 + exploding), x, y);

            exploding ++;
        }
    }

    protected void hit(GameState state) {
        exploding = 0;
    }
}

package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Level;
import duke.Renderer;
import duke.active.enemies.Explosion;

import static duke.Gfx.TILE_SIZE;

public class Dynamite extends Active {
    private int fuse;

    private int explosions;

    private boolean left, right;

    public Dynamite(int x, int y) {
        super(x, y);

        fuse = 16;
        explosions = 0;
    }

    @Override
    public void update(GameState state) {
        super.update(state);

        fuse--;

        if (fuse <= 0) {
            explode(state);
        }
    }

    private void explode(GameState state) {
        if (explosions == 0) {
            state.spawn(new Explosion(x, y));

            left = true;
            right = true;
        } else if (explosions < 6) {
            if (left) {
                int leftX = x - (explosions * TILE_SIZE);
                if (canExplode(state.getLevel(), leftX, y)) {
                    state.spawn(new Explosion(leftX, y));
                } else {
                    left = false;
                }
            }

            if (right) {
                int rightX = x + (explosions * TILE_SIZE);
                if (canExplode(state.getLevel(), rightX, y)) {
                    state.spawn(new Explosion(rightX, y));
                } else {
                    right = false;
                }
            }
        } else {
            active = false;
        }

        if ((fuse % 2) == 0) {
            explosions++;
        }
    }

    private boolean canExplode(Level level, int x, int y) {
        int row = y / TILE_SIZE;
        int col = x / TILE_SIZE;

        boolean free = !level.isSolid(row, col);
        boolean solidGround = level.isSolid(row + 1, col);

        return free && solidGround;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        if (fuse > 0) {
            renderer.drawTile(assets.getAnim(116 + (fuse % 2)), x, y);
        }
    }
}

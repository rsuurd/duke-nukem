package duke.active;

import duke.Duke;
import duke.GameState;
import duke.Level;
import duke.effects.Effect;

import static duke.Gfx.TILE_SIZE;

public class Bridge extends Active {
    public static final int BRIDGE_SOLID_TILE_ID = 0x21A0;

    private int health;
    private boolean onTop;

    public Bridge(int x, int y, int columns) {
        super(x, y);

        width = (columns * TILE_SIZE) - 1;
        health = 2;
    }

    @Override
    public void update(GameState state) {
        Duke duke = state.getDuke();

        if (onTop(duke)) {
            if (!onTop) {
                hit(state);
            }
        }

        onTop = onTop(duke);
    }

    private boolean onTop(Duke duke) {
        return (duke.getX() >= x) && ((duke.getX() + duke.getWidth()) <= (x + width)) && (duke.getY() + duke.getHeight() + 1) == y;
    }

    @Override
    public void hit(GameState state) {
        health --;

        if (health == 0) {
            Level level = state.getLevel();
            int row = y / TILE_SIZE;
            int col = x / TILE_SIZE;

            for (int i = 0; i <= (i + width) / TILE_SIZE; i++) {
                level.setTile(row, col + i, level.getTile(row - 1, col + i));

                if ((i % 2) == 1) {
                    int effectX = x + (i * TILE_SIZE);

                    state.addEffect(new Effect.Sparks(effectX, y));
                    Effect.Particle.createParticles(state, effectX + TILE_SIZE, y);
                }
            }

            active = false;
        }
    }

    public static Bridge create(int location, int[] tiles) {
        int columns = 0;
        boolean closed = false;

        while (!closed && (location + columns) < tiles.length) {
            tiles[location + columns] = BRIDGE_SOLID_TILE_ID + (columns % 2) * 32;

            closed = Level.isSolid(tiles[location + columns + 1]);

            if (!closed) {
                columns++;
            }
        }

        int x = (location % Level.WIDTH) * TILE_SIZE;
        int y = (location / Level.WIDTH) * TILE_SIZE;

        return new Bridge(x, y, columns);
    }
}

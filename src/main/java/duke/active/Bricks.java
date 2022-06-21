package duke.active;

import duke.GameState;
import duke.effects.Effect;

import static duke.Gfx.TILE_SIZE;

public class Bricks extends Active {
    public Bricks(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean canBeShot() {
        return true;
    }

    @Override
    public void hit(GameState state) {
        state.increaseScore(10);
        state.addEffect(new Effect.Sparks(x, y));
        state.addEffect(new Effect.Smoke(x, y));

        state.getLevel().setTile(y / TILE_SIZE, x / TILE_SIZE, 0x17E0);

        active = false;
    }
}

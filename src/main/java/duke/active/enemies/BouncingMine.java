package duke.active.enemies;

import duke.Assets;
import duke.GameState;
import duke.Renderer;
import duke.active.Active;

public class BouncingMine extends Active {
    private int bouncing;

    public BouncingMine(int x, int y) {
        super(x, y);

        bouncing = 0;
    }

    @Override
    public void update(GameState state) {
        if (bouncing == 0) {
            velocityY = -16;
        } else if (bouncing < 4) {
            velocityY += 4;
        } else if (bouncing < 6) {
            velocityY = -1;
        } else if (bouncing < 8) {
            velocityY = 0;
        } else if (bouncing < 10) {
            velocityY = 1;
        } else {
            velocityY += 4;
        }

        super.update(state);

        if (state.getDuke().collidesWith(this)) {
            state.getDuke().hurt();
        }

        bouncing = (bouncing + 1) % 14;
    }

    @Override
    public boolean canBeShot() {
        return true;
    }

    @Override
    protected void applyGravity() {}

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getAnim(231), x, y);
    }
}

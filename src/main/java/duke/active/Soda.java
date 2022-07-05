package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;
import duke.effects.Effect;

public class Soda extends Item {
    private int frame;

    private boolean fizzing;

    public Soda(int x, int y) {
        super(x, y, 132, 200);

        fizzing = false;
    }

    @Override
    public void update(GameState state) {
        if (fizzing) {
            velocityY = -4;
        }

        super.update(state);
    }

    @Override
    protected void bump(GameState state) {
        active = false;

        state.addEffect(new Effect.Sparks(x, y));
    }

    @Override
    protected void pickedUp(GameState state) {
        if (fizzing) {
            points = 1000;
        } else {
            state.getHints().showHint(this);

            state.getDuke().increaseHealth(1);
        }

        super.pickedUp(state);
    }

    @Override
    public boolean canBeShot() {
        return !fizzing;
    }

    @Override
    public void hit(GameState state) {
        fizzing = true;
        frame = 0;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        int tileIndex = fizzing ? 136 : 132;

        renderer.drawTile(assets.getAnim(tileIndex + (frame / 4)), x, y);

        frame = (frame + 1) % 16;
    }
}

package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;

public class Turkey extends Item {
    private boolean whole;

    public Turkey(int x, int y) {
        super(x, y, 44, 100);

        whole = false;
    }

    @Override
    public boolean canBeShot() {
        return !whole;
    }

    @Override
    public void hit(GameState state) {
        super.hit(state);

        whole = true;
    }

    @Override
    protected void pickedUp(GameState state) {
        state.getHints().showHint(this);

        points = whole ? 200 : 100;

        super.pickedUp(state);

        state.getDuke().increaseHealth(whole ? 2 : 1);
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getObject(whole ? 45 : 44), x, y);
    }
}

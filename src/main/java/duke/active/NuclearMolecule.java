package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;

import static duke.Duke.MAX_HEALTH;

public class NuclearMolecule extends Item {
    public NuclearMolecule(int x, int y) {
        super(x, y, 74, 1000);
    }

    @Override
    protected void pickedUp(GameState state) {
        super.pickedUp(state);

        state.getDuke().increaseHealth(MAX_HEALTH);
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        super.render(renderer, assets);

        frame = (frame + 1) % 9;
    }
}

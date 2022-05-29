package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Gfx;
import duke.Renderer;

public abstract class Active {
    protected int x;
    protected int y;

    protected boolean active;

    protected Active(int x, int y) {
        this.x = x;
        this.y = y;

        active = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isActive() {
        return active;
    }

    public void update(GameState state) {}

    public void render(Renderer renderer, Assets assets) {}
}

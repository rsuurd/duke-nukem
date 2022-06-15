package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;
import duke.effects.Effect;

public class Box extends Active {
    static final int GREY = 0;
    static final int BLUE = 100;
    static final int RED = 101;

    private int tileIndex;

    private Active contents;

    public Box(int tileIndex, int x, int y) {
        this(tileIndex, x, y, null);
    }

    public Box(int tileIndex, int x, int y, Active contents) {
        super(x, y);

        this.tileIndex = tileIndex;
        this.contents = contents;
    }

    @Override
    public void update(GameState state) {
        super.update(state);

        checkHit(state);
    }

    @Override
    protected void hit(GameState state) {
        if (contents != null) {
            contents.moveTo(x, y);

            state.spawn(contents);
        }

        Effect.Particle.createParticles(state, x, y);

        active = false;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getObject(tileIndex), x, y);
    }
}

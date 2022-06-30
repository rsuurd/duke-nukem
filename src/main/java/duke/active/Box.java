package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;
import duke.effects.Effect;

public class Box extends Active {
    public static final int GREY = 0;
    public static final int BLUE = 100;
    public static final int RED = 101;

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
    public void moveTo(int x, int y) {
        if (contents != null) {
            int offsetX = contents.getX() - this.x;
            int offsetY = contents.getY() - this.y;

            contents.moveTo(x + offsetX, y + offsetY);
        }

        super.moveTo(x, y);
    }

    @Override
    public boolean canBeShot() {
        return true;
    }

    @Override
    public void hit(GameState state) {
        if (contents != null) {
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

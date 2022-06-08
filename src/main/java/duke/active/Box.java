package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;

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
        if (!state.getLevel().collides(x, y + 8, 15, 15)) {
            y += 8;
        }

        state.getBolts().stream().filter(bolt -> bolt.hits(this)).findFirst().ifPresent(bolt -> {
            bolt.hit();

            if (contents != null) {
                contents.moveTo(x, y);

                state.spawn(contents); // FIXME
            }

            active = false;
        });
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getObject(tileIndex), x, y);
    }
}

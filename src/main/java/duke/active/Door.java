package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Level;
import duke.Renderer;

import static duke.Gfx.TILE_SIZE;

public class Door extends Active {
    public static final int RED_DOOR_TILE_ID = 0x304C;
    public static final int GREEN_DOOR_TILE_ID = 0x304D;
    public static final int BLUE_DOOR_TILE_ID = 0x304E;
    public static final int MAGENTA_DOOR_TILE_ID = 0x304F;

    private Key.Type type;

    private boolean opening;
    private int frame;

    public Door(int x, int y, Key.Type type) {
        super(x, y);

        this.type = type;

        frame = 0;
    }

    public Key.Type getType() {
        return type;
    }

    @Override
    public void update(GameState state) {
        if (opening) {
            frame ++;
        }

        if (frame >= 8) {
            active = false;
        }
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getObject(128 + frame), x, y);
    }

    public void open(GameState state) {
        Level level = state.getLevel();

        int row = y / TILE_SIZE;
        int col = x / TILE_SIZE;

        level.setTile(row, col, level.getTile(row, col - 1));

        opening = true;
    }
}

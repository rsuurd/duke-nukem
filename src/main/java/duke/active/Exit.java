package duke.active;

import duke.Duke;
import duke.GameState;
import duke.Level;

import static duke.Gfx.TILE_SIZE;
import static duke.active.Exit.State.*;

public class Exit extends Active {
    public static final int EXIT_DOOR_TILE_ID = 0x10001;

    private State state;

    private int frame;

    protected Exit(int x, int y) {
        super(x, y);

        width = 2 * TILE_SIZE;
        height = 2 * TILE_SIZE;

        state = State.CLOSED;
        frame = 0;
    }

    @Override
    public void update(GameState state) {
        switch (this.state) {
            case CLOSED -> check(state);
            case OPENING -> open(state);
            case CLOSING -> close(state);
        }
    }

    private void check(GameState state) {
        Duke duke = state.getDuke();

        if ((x <= duke.getX()) && ((x + width) >= (duke.getX() + duke.getWidth())) &&
                (y <= duke.getY()) && ((y + height) >= (duke.getY() + duke.getHeight()))) {
            state.getHints().showHint(this);

            if (duke.isUsing()) {
                this.state = OPENING;
            }
        }
    }

    private void open(GameState state) {
        frame++;

        updateDoor(state.getLevel(), x, y, EXIT_DOOR_TILE_ID + (frame * 4));

        if (frame == 3) {
            this.state = CLOSING;
        }
    }

    private void close(GameState state) {
        frame--;

        updateDoor(state.getLevel(), x, y, EXIT_DOOR_TILE_ID + (frame * 4));

        if (frame == 0) {
            this.state = CLOSED;

            state.goToNextLevel();
        }
    }

    public static Exit create(int x, int y, Level level) {
        int worldY = y - TILE_SIZE;

        updateDoor(level, x, worldY, EXIT_DOOR_TILE_ID);

        return new Exit(x, worldY);
    }

    private static void updateDoor(Level level, int x, int y, int tileId) {
        int row = y / TILE_SIZE;
        int col = x / TILE_SIZE;

        level.setTile(row, col, tileId);
        level.setTile(row, col + 1, tileId + 1);
        level.setTile(row + 1, col, tileId + 2);
        level.setTile(row + 1, col + 1, tileId + 3);
    }

    enum State {
        CLOSED,
        OPENING,
        CLOSING
    }
}

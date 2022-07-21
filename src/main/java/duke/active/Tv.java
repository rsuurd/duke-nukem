package duke.active;

import duke.GameState;
import duke.Level;
import duke.modals.Modal;

import static duke.Gfx.TILE_SIZE;
import static duke.modals.Modal.EXIT_ON_ENTER;

public class Tv extends Active {
    public static final int TV_TILE_ID = 0x10017;

    private State state;

    private int frame;

    private Tv(int x, int y) {
        super(x, y);

        width = 2 * TILE_SIZE;
        height = 2 * TILE_SIZE;

        state = State.OFF;
    }

    @Override
    public void update(GameState state) {
        if (state.getViewport().isVisible(this)) {
            if (this.state == State.OFF) {
                broadcast(state);
            }
        } else if (this.state == State.TRACKING) {
            this.state = State.STATIC;
        }

        if (state.getDuke().collidesWith(this) && state.getDuke().isUsing()) {
            broadcast(state);
        }

        frame = switch (this.state) {
            case OFF -> 0;
            case STATIC -> 1 + ((frame + 1) % 3);
            case TRACKING -> 5 + Integer.signum(state.getDuke().getX() / TILE_SIZE - x / TILE_SIZE);
        };

        updateTv(state.getLevel(), x, y, frame);
    }

    private void broadcast(GameState state) {
        this.state = State.TRACKING;

        state.showModal(new Modal(TILE_SIZE, 48, getMessage(state.getLevel().getNumber()), EXIT_ON_ENTER));
    }

    private String getMessage(int level) {
        return switch (level) {
            case 2 -> "Surprise Duke, I'll be\ntracking your every move\nwith my security camera\nsystem!\n\n      Press ENTER:";
            default -> null;
        };
    }

    public static Tv create(int x, int y, Level level) {
        int row = y / TILE_SIZE;
        int col = x / TILE_SIZE;

        level.setTile(row, col, TV_TILE_ID);
        level.setTile(row, col + 1, TV_TILE_ID + 1);

        updateTv(level, x, y - TILE_SIZE, 1);

        return new Tv(x, y - TILE_SIZE);
    }

    private static void updateTv(Level level, int x, int y, int frame) {
        int row = y / TILE_SIZE;
        int col = x / TILE_SIZE;

        int offset = frame * 2;

        level.setTile(row, col, TV_TILE_ID + offset);
        level.setTile(row, col + 1, TV_TILE_ID + offset + 1);
    }

    enum State {
        OFF,
        TRACKING,
        STATIC
    }
}

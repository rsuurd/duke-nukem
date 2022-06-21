package duke.active;

import duke.*;

import static duke.Gfx.TILE_SIZE;

public class Elevator extends Active {
    public static final int TILE_ID = 0x3001;

    private int elevation;

    public Elevator(int x, int y) {
        super(x, y);

        elevation = 0;
    }

    @Override
    public void update(GameState state) {
        if (state.getDuke().isOnTop(this)) {
            if (state.getDuke().isUsing()) {
                goUp(state);
            }
        } else {
            goDown(state);
        }
    }

    private void goUp(GameState state) {
        Duke duke = state.getDuke();
        Level level = state.getLevel();

        int col = x / TILE_SIZE;
        if (!level.isSolid((duke.getY() / TILE_SIZE) - 1, col)) {
            elevation ++;
            y -= TILE_SIZE;

            level.setTile(y / TILE_SIZE, col, TILE_ID);
            duke.moveTo(duke.getX(), duke.getY() - TILE_SIZE);
        }
    }

    private void goDown(GameState state) {
        if (elevation > 0) {
            Level level = state.getLevel();

            int row = y / TILE_SIZE;
            int col = x / TILE_SIZE;

            level.setTile(row, col, level.getTile(row - 1, col));

            elevation --;
            y += TILE_SIZE;
        }
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        for (int worldY = y + (elevation * TILE_SIZE); worldY >= y; worldY--) {
            renderer.drawTile(assets.getObject(5), x, worldY);
        }
    }
}

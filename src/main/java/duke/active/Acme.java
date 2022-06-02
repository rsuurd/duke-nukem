package duke.active;

import duke.*;

import static duke.Gfx.TILE_SIZE;

public class Acme extends Active {
    private static final int FALL_SPEED = 14;

    private State state;
    private int shaker;

    public Acme(int x, int y) {
        super(x, y);

        state = State.IDLE;
    }

    @Override
    public void update(GameState state) {
        Duke duke = state.getDuke();

        switch (this.state) {
            case IDLE -> checkDrop(duke);
            case SHAKING -> shake();
            case FALLING -> fall(state.getLevel());
        }
    }

    private void checkDrop(Duke duke) {
        if (y < duke.getY() && (x < duke.getX()) && ((x + 31) > duke.getX())) {
            state = State.SHAKING;
        }
    }

    private void shake() {
        shaker++;

        y = y + (((shaker % 2) == 0) ? -1 : 1);

        if (shaker > 14) {
            state = State.FALLING;
        }
    }

    private void fall(Level level) {
        y += FALL_SPEED;

        if (level.collides(x, y + TILE_SIZE - 1, 31, 0)) {
            crash();
        }
    }

    private void crash() {
        active = false;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getObject(83), x, y);
        renderer.drawTile(assets.getObject(84), x + TILE_SIZE, y);
    }

    enum State {
        IDLE, SHAKING, FALLING;
    }
}

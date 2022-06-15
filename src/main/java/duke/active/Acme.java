package duke.active;

import duke.Assets;
import duke.Duke;
import duke.GameState;
import duke.Renderer;

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
            case FALLING -> super.update(state);
        }

        checkHit(state);
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

    @Override
    protected void applyGravity() {
        if (this.state == State.FALLING) {
            velocityY = FALL_SPEED;
        }
    }

    @Override
    protected void hit(GameState state) {
        state.increaseScore(500);
    }

    @Override
    protected void land(GameState state) {
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

package duke.active.enemies;

import duke.Assets;
import duke.Facing;
import duke.GameState;
import duke.Renderer;
import duke.effects.Effect;

public class Techbot extends PatrolBot {
    private static final int DEATH_TIMER = 7;

    private int frame;
    private int exploding;

    public Techbot(int x, int y) {
        super(x, y, Facing.RIGHT, 8);

        frame = 0;
        exploding = -1;
    }

    @Override
    public void update(GameState state) {
        if (isAlive()) {
            super.update(state);
        } else {
            if (exploding == DEATH_TIMER) {
                state.increaseScore(100);
                state.addEffect(new Effect.Sparks(x, y));

                active = false;
            }
        }
    }

    @Override
    protected void patrol(GameState state) {
        if (frame == 0) {
            super.patrol(state);
        }
    }

    private boolean isAlive() {
        return exploding < 0;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        if (isAlive()) {
            renderer.drawTile(assets.getAnim(82 + frame), x, y);

            frame = (frame + 1) % 3;
        } else {
            renderer.drawTile(assets.getAnim(85 + exploding), x, y);

            exploding ++;
        }
    }

    public void hit(GameState state) {
        exploding = 0;
    }
}

package duke.active.enemies;

import duke.Assets;
import duke.Facing;
import duke.GameState;
import duke.Renderer;
import duke.effects.Effect;

import static duke.Gfx.TILE_SIZE;

public class FlameWheelBot extends PatrolBot {
    private int frame;
    private int flame;

    private int health ;

    public FlameWheelBot(int x, int y) {
        super(x, y, Facing.LEFT, 8);

        health = 2;
    }

    @Override
    public void update(GameState state) {
        super.update(state);

        if ((health < 2) && (frame % 4) == 0) {
            state.addEffect(new Effect.Smoke(x + 8, y - 8));
        }

        frame = (frame + 1) % 4;
        flame = (flame + 1) % 64;
    }

    @Override
    protected void patrol(GameState state) {
        if ((frame % 2) == 0) {
            super.patrol(state);
        }
    }

    private boolean isFlameOn() {
        return flame >= 32;
    }

    @Override
    public void hit(GameState state) {
        if (!isFlameOn()) {
            health --;

            if (health == 0) {
                state.increaseScore(200);
                state.addEffect(new Effect.Smoke(x + 8, y - 8));
                state.addEffect(new Effect.Sparks(x + 8, y - 8));
                Effect.Particle.createParticles(state, x + 8, y - 8);

                // TODO play sound

                active = false;
            }
        }
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        int baseTile = (isFlameOn() ? 66 : 50) + (frame * 4);

        renderer.drawTile(assets.getAnim(baseTile), x - 8, y - TILE_SIZE);
        renderer.drawTile(assets.getAnim(baseTile + 1), x + 8, y - TILE_SIZE);
        renderer.drawTile(assets.getAnim(baseTile + 2), x - 8, y);
        renderer.drawTile(assets.getAnim(baseTile + 3), x + 8, y);
    }
}

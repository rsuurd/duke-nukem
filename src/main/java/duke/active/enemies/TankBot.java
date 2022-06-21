package duke.active.enemies;

import duke.Assets;
import duke.Facing;
import duke.GameState;
import duke.Renderer;
import duke.effects.Effect;

import static duke.Gfx.TILE_SIZE;

public class TankBot extends PatrolBot {
    private int health;

    private int frame;

    public TankBot(int x, int y) {
        super(x, y, Facing.LEFT, 4);

        width = (2 * TILE_SIZE) - 1;
        health = 2;

        frame = 0;
    }

    @Override
    public void update(GameState state) {
        super.update(state);

        if ((health == 1) && ((frame % 6) == 0)) {
            int originX = x + ((facing == Facing.LEFT) ? TILE_SIZE : 0);

            state.addEffect(new Effect.Smoke(originX, y - 8));
        }
    }

    @Override
    protected void patrol(GameState state) {
        Facing prev = facing;

        super.patrol(state);

        if ((prev != facing) && (canSeeDuke(state))) {
            int originX = x + ((facing == Facing.LEFT) ? 0 : TILE_SIZE);

            state.spawn(new Shot(originX, y - 4, facing));
        }
    }

    private boolean canSeeDuke(GameState state) {
        int reach = 10 * TILE_SIZE;

        int x = (this.x + TILE_SIZE) + ((facing == Facing.LEFT) ? -reach : 0);

        return state.getDuke().collidesWith(x, this.y, reach, height);
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        int tileId = ((facing ==Facing.LEFT) ? 34 : 38) + ((frame / 4) * 2);

        renderer.drawTile(assets.getAnim(tileId), x, y);
        renderer.drawTile(assets.getAnim(tileId + 1), x + TILE_SIZE, y);

        frame = (frame + 1) % 8;
    }

    @Override
    public void hit(GameState state) {
        health --;

        if (health == 0) {
            int centerX = x + TILE_SIZE;

            state.increaseScore(500); // TODO verify
            state.addEffect(new Effect.Sparks(centerX, y));
            state.addEffect(new Effect.Smoke(centerX, y));
            Effect.Particle.createParticles(state, centerX, y);
            Effect.Particle.createParticles(state, centerX, y);

            active = false;
        }
    }
}

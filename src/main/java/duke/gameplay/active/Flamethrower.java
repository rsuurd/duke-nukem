package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.*;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.Renderable;
import duke.gfx.SpriteRenderer;
import duke.level.Level;
import duke.sfx.Sfx;

import static duke.gfx.SpriteDescriptor.OBJECTS;

public class Flamethrower extends Active implements Updatable {
    private Facing facing;
    private State state;
    private int timer;

    public Flamethrower(int x, int y, Facing facing) {
        this(x, y, State.IDLE);

        this.facing = facing;
    }

    Flamethrower(int x, int y, State state) {
        super(x, y, 0, 0);

        this.state = state;
        this.timer = 0;
    }

    @Override
    public void update(GameplayContext context) {
        if (timer++ >= state.duration) {
            switchState(getNextState(), context);
        }
    }

    private State getNextState() {
        return switch (state) {
            case IDLE -> State.IGNITE;
            case IGNITE -> State.BURN;
            case BURN -> State.IDLE;
        };
    }

    private void switchState(State next, GameplayContext context) {
        this.state = next;
        timer = 0;

        if (state == State.IGNITE) {
            context.getSoundManager().play(Sfx.TORCH_ON);
            context.getActiveManager().spawn(EffectsFactory.createIgnitionEffect(getX(), getY(), facing, state.duration));
        } else if (state == State.BURN) {
            int x = facing == Facing.RIGHT ? getX() : getX() - (2 * Level.TILE_SIZE);

            context.getActiveManager().spawn(new Flame(x, getY(), facing, state.duration));
        }
    }

    State getState() {
        return state;
    }

    enum State {
        IDLE(32),
        IGNITE(10),
        BURN(20);

        int duration;

        State(int duration) {
            this.duration = duration;
        }
    }

    static class Flame extends Active implements Updatable, Damaging, Renderable {
        private Facing facing;
        private int ttl;
        private int animationFrame;

        public Flame(int x, int y, Facing facing, int ttl) {
            super(x, y, 3 * Level.TILE_SIZE, Level.TILE_SIZE);

            this.facing = facing;
            this.ttl = ttl;

            animationFrame = 0;
        }

        @Override
        public void update(GameplayContext context) {
            animationFrame = (animationFrame + 1) % 2;

            if (--ttl <= 0) {
                destroy();
            }
        }

        @Override
        public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
            int baseIndex = (facing == Facing.RIGHT) ? FLAME_RIGHT_TILE_BASE : FLAME_LEFT_TILE_BASE;
            int[] offsets = (facing == Facing.RIGHT) ? RIGHT_TILE_OFFSETS : LEFT_TILE_OFFSETS;

            for (int i = 0; i < offsets.length; i++) {
                int offset = offsets[i];

                spriteRenderer.render(renderer, OBJECTS, baseIndex + offset + animationFrame, screenX + (i * Level.TILE_SIZE), screenY);
            }
        }
    }

    private static final int FLAME_RIGHT_TILE_BASE = 25;
    private static final int FLAME_LEFT_TILE_BASE = 30;

    private static final int[] LEFT_TILE_OFFSETS = {2, 0, 0};
    private static final int[] RIGHT_TILE_OFFSETS = {0, 0, 2};
}

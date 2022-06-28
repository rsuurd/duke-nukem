package duke.active.enemies;

import duke.Assets;
import duke.GameState;
import duke.Renderer;
import duke.active.Active;
import duke.effects.Debris;
import duke.effects.Effect;

import java.awt.image.BufferedImage;

import static duke.Gfx.TILE_SIZE;

public class Reactor extends Active {
    private int health;

    private int frame;

    private boolean hit;

    private int destruction;

    public Reactor(int x, int y) {
        super(x, y - (2 * TILE_SIZE));

        height = 3 * TILE_SIZE;
        health = 10;

        frame = 0;

        hit = false;
        destruction = -1;
    }

    @Override
    public void update(GameState state) {
        if (destruction >= 0) {
            destructionSequence(state);
        } else if (health == 0) {
            destruction = 0;
        } else if (health > 0) {
            if (state.getDuke().collidesWith(this)) {
                state.getDuke().kill();
            }

            frame = (frame + 1) % 5;
        }
    }

    private void destructionSequence(GameState state) {
        switch (destruction) {
            case 6 -> state.addEffect(new Effect.Sparks(x - 16, y + 16));
            case 12 -> {
                state.addEffect(new Effect.Sparks(x, y - 16));
                state.addEffect(new Effect.Sparks(x, y + 48));
                state.addEffect(new Effect.Sparks(x - 8, y + 80));
                state.addEffect(new Effect.Flash(x + 16, y + 32));
                state.addEffect(new Effect.Flash(x, y + 48));
            }
            case 16 -> state.addEffect(new Effect.Sparks(x + 16, y + 16));
            case 18 -> {
                state.addEffect(new Effect.Sparks(x - 24, y));
                state.addEffect(new Effect.Sparks(x + 4, y + 64));
                state.addEffect(new Effect.Sparks(x + 16, y + 32));
                Effect.Particle.createParticles(state, x, y + 32);
            }
            case 24 -> {
                state.addEffect(new Effect.Flash(x, y - 16));
                state.addEffect(new Effect.Flash(x + 16, y + 24));
                state.addEffect(new Effect.Flash(x , y + 48));
                state.addEffect(new Effect.Sparks(x + 4, y + 64));
                state.addEffect(new Effect.Sparks(x + 16, y + 32));
            }
            case 25 -> {
                state.addEffect(new Effect.Sparks(x - 4, y + 32));
                state.addEffect(new Effect.Sparks(x + 24, y + 32));
                state.addEffect(new Effect.Flash(x - 24, y));
                state.addEffect(new Debris(x + 24, y + 32));
                Effect.Particle.createParticles(state, x + 16, y + 24);
                Effect.Particle.createParticles(state, x - 4, y + 72);
            }
            case 26 -> {
                Effect.Particle.createParticles(state, x - 8, y - 8);
                Effect.Particle.createParticles(state, x + 24, y - 4);
                Effect.Particle.createParticles(state, x + 12, y + 48);
            }
            case 30 -> {
                state.addEffect(new Effect.Sparks(x, y - 16));
                state.addEffect(new Effect.Sparks(x, y + 48));
                state.addEffect(new Effect.Sparks(x + 8, y + 64));
                state.addEffect(new Effect.Flash(x + 16, y + 16));
                state.addEffect(new Effect.Flash(x - 16, y + 80));
            }
            case 31 -> {
                Effect.Particle.createParticles(state, x - 4, y + 72);
                Effect.Particle.createParticles(state, x + 16, y + 32);
                state.addEffect(new Debris(x + 24, y + 8));
                state.addEffect(new Debris(x + 16, y + 64));
            }
            case 32 -> {
                state.addEffect(new Effect.Flash(x - 24, y));
                state.addEffect(new Effect.Flash(x + 16, y + 32));
                state.addEffect(new Effect.Sparks(x - 4, y + 30));
            }
            case 34 -> {
                state.addEffect(new Debris(x - 8, y + 24));
                state.increaseScore(20000);
                state.addEffect(new Effect.Score(x - 8, y + 16, 10000));
                state.addEffect(new Effect.Score(x + 8, y + 16, 10000));

                active = false;
            }
            default -> {}
        }

        if ((destruction % 4) == 0) {
            state.addEffect(new Effect.Smoke(x - 8, y + 32));
        } else if ((destruction % 4) == 2) {
            state.addEffect(new Effect.Smoke(x + 8, y + 32));
        }

        destruction ++;
    }

    @Override
    public boolean canBeShot() {
        return health > 0;
    }

    @Override
    public void hit(GameState state) {
        health --;
        hit = true;

        Effect.Particle.createParticles(state, x, y + TILE_SIZE);
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        BufferedImage sprite = hit ? assets.getAnim(265) : assets.getObject(90 + frame);

        renderer.drawTile(sprite, x, y);
        renderer.drawTile(sprite, x, y + TILE_SIZE);
        renderer.drawTile(sprite, x, y + (2 * TILE_SIZE));

        hit = false;
    }
}

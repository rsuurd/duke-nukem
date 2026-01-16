package duke.gameplay.effects;

import duke.Renderer;
import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Updatable;
import duke.gfx.Renderable;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderer;
import duke.level.Level;
import duke.sfx.Sfx;

public class ReactorDestructionSequence extends Active implements Updatable, Renderable {
    private SpriteDescriptor spriteDescriptor;

    private int timer;

    public ReactorDestructionSequence(int x, int y, int height, SpriteDescriptor spriteDescriptor) {
        super(x, y, Level.TILE_SIZE, height);

        this.spriteDescriptor = spriteDescriptor;
    }

    @Override
    public void update(GameplayContext context) {
        smoke(context);
        switch (++timer) {
            case 6 -> sparks(context, -16, 16);
            case 12 -> {
                sparks(context, 0, -16);
                sparks(context, 0, 48);
                sparks(context, -8, 80);
                flash(context, 16, 32);
                flash(context, 0, 48);
                explode(context);
            }
            case 16 -> sparks(context, 16, 16);
            case 18 -> {
                sparks(context, -24, 0);
                sparks(context, 4, 64);
                sparks(context, 16, 32);
                particles(context, 0, 32);
                explode(context);
            }
            case 24 -> {
                flash(context, 0, -16);
                flash(context, 16, 24);
                flash(context, 0, 48);
                sparks(context, 4, 64);
                sparks(context, 16, 32);
            }
            case 25 -> {
                sparks(context, -4, 32);
                sparks(context, 24, 32);
                flash(context, -24, 0);
                debris(context, 24, 32);
                particles(context, 16, 24);
                particles(context, -4, 72);
            }
            case 26 -> {
                particles(context, -8, -8);
                particles(context, 24, -4);
                particles(context, 12, 48);
                explode(context);
            }
            case 30 -> {
                sparks(context, 0, -16);
                sparks(context, 0, 48);
                sparks(context, 8, 64);
                flash(context, 16, 16);
                flash(context, -16, 80);
            }
            case 31 -> {
                particles(context, -4, 72);
                particles(context, 16, 32);
                debris(context, 24, 8);
                debris(context, 16, 64);
                explode(context);
            }
            case 32 -> {
                flash(context, -24, 0);
                flash(context, 16, 32);
                sparks(context, -4, 30);
            }
            case DESTRUCTION_TIME -> destroy(context);
            default -> {
            }
        }
    }

    private void sparks(GameplayContext context, int offsetX, int offsetY) {
        context.getActiveManager().spawn(EffectsFactory.createSparks(getX() + offsetX, getY() + offsetY));
    }

    private void flash(GameplayContext context, int offsetX, int offsetY) {
        context.getActiveManager().spawn(EffectsFactory.createFlash(getX() + offsetX, getY() + offsetY));
    }

    private void particles(GameplayContext context, int offsetX, int offsetY) {
        context.getActiveManager().spawn(EffectsFactory.createParticles(getX() + offsetX, getY() + offsetY));
    }

    private void debris(GameplayContext context, int offsetX, int offsetY) {
        context.getActiveManager().spawn(EffectsFactory.createDebris(getX() + offsetX, getY() + offsetY));
    }

    private void smoke(GameplayContext context) {
        if (timer % 4 == 0) {
            context.getActiveManager().spawn(EffectsFactory.createSmoke(getX() - 8, getY() + 32));
        } else if (timer % 4 == 2) {
            context.getActiveManager().spawn(EffectsFactory.createSmoke(getX() + 12, getY() + getHeight() - 16));
        }
    }

    private void explode(GameplayContext context) {
        context.getSoundManager().play(Sfx.BOX_EXPLODE);
    }

    private void destroy(GameplayContext context) {
        context.getActiveManager().spawn(new Debris(getX() - 8, getY() + 24));
        context.getScoreManager().score(10000, getX() - 8, getY() + 16);
        context.getScoreManager().score(10000, getX() - 8, getY() + 16);
        context.getSoundManager().play(Sfx.HIT_REACTOR);

        destroy();
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        for (int y = screenY; y < screenY + getHeight(); y += Level.TILE_SIZE) {
            spriteRenderer.render(renderer, spriteDescriptor, screenX, y);
        }
    }

    static final int DESTRUCTION_TIME = 34;
}

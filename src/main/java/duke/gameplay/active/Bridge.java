package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Solid;
import duke.gameplay.Updatable;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.Renderable;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderer;
import duke.sfx.Sfx;

import static duke.gfx.SpriteDescriptor.TILES;
import static duke.level.Level.TILE_SIZE;

public class Bridge extends Active implements Solid, Updatable, Renderable {
    private int health;
    private boolean steppedOn;

    public Bridge(int x, int y, int width) {
        super(x, y, width, TILE_SIZE);

        health = 2;
        steppedOn = false;
    }

    @Override
    public void update(GameplayContext context) {
        boolean hasOnTop = hasOnTop(context.getPlayer());

        if (!steppedOn && hasOnTop) {
            health--;
        }

        steppedOn = hasOnTop;

        if (health <= 0) {
            destroy(context);
        }
    }

    private void destroy(GameplayContext context) {
        for (int x = getX() + TILE_SIZE; x < getX() + getWidth(); x += TILE_SIZE * 2) {
            context.getActiveManager().spawn(EffectsFactory.createSparks(x, getY()));
            context.getActiveManager().spawn(EffectsFactory.createParticles(x + TILE_SIZE, getY()));
        }
        context.getSoundManager().play(Sfx.BOX_EXPLODE);
        destroy();
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        for (int x = screenX; x < screenX + getWidth(); x += TILE_SIZE) {
            spriteRenderer.render(renderer, DESCRIPTOR, x, screenY);
        }
    }

    private static final SpriteDescriptor DESCRIPTOR = new SpriteDescriptor(TILES, 269);
}

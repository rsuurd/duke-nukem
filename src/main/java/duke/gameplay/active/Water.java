package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Layer;
import duke.gameplay.Updatable;
import duke.gfx.Renderable;
import duke.gfx.SpriteRenderer;
import duke.gfx.Viewport;

import static duke.level.Level.TILE_SIZE;

public class Water extends Active implements Updatable, Renderable {
    private int tick;

    public Water(int x, int y) {
        super(x, y, TILE_SIZE, 2 * TILE_SIZE);
    }

    @Override
    public void update(GameplayContext context) {
        tick = (tick + 1) % 2;
    }

    @Override
    public Layer getLayer() {
        return Layer.POST_PROCESS;
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        if (screenX >= Viewport.WIDTH || screenY >= Viewport.HEIGHT) return;

        int destHeight = getHeight();
        int sourceHeight = (getHeight() * 3) / 2; // the water reflects 3 tiles in 2 tiles for a squeezed effect

        int sourceY = 0;
        int remaining = 0;

        for (int y = 0; y < destHeight; y++) {
            int sampleY = screenY - (1 + tick + sourceY);

            if (sampleY < 0) return;

            for (int x = 0; x < getWidth(); x++) {
                renderer.copy(screenX + x, sampleY, screenX + x, screenY + y);
            }

            remaining += sourceHeight;
            while (remaining >= destHeight) {
                sourceY++;
                remaining -= destHeight;
            }
        }
    }
}

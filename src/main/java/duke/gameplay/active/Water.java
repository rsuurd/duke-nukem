package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Updatable;
import duke.gfx.Renderable;
import duke.gfx.Sprite;
import duke.gfx.SpriteRenderer;
import duke.gfx.Viewport;

import java.util.Arrays;

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
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        // TODO take pixels to simulate reflections
        if (screenX < Viewport.WIDTH && screenY < Viewport.HEIGHT) {
            renderer.draw(SPRITE, screenX, screenY);
        }
    }

    private static final Sprite SPRITE = new Sprite(TILE_SIZE, 2 * TILE_SIZE);

    static {
        Arrays.fill(SPRITE.getPixels(), (byte) 1);
    }
}

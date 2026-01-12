package duke.gfx;

import duke.Renderer;
import duke.gameplay.Layer;

public interface Renderable {
    void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY);

    default Layer getLayer() {
        return Layer.FOREGROUND;
    }

    default boolean isVisible() {
        return true;
    }
}

package duke.gfx;

import duke.Renderer;

public interface SpriteRenderable extends Renderable{
    SpriteDescriptor getSpriteDescriptor();

    @Override
    default void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        spriteRenderer.render(renderer, this, screenX, screenY);
    }
}

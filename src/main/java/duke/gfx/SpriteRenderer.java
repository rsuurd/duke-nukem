package duke.gfx;

import duke.Renderer;
import duke.resources.AssetManager;

import java.util.List;

public class SpriteRenderer {
    private AssetManager assets;

    public SpriteRenderer(AssetManager assets) {
        this.assets = assets;
    }

    public void render(Renderer renderer, SpriteRenderable renderable, int x, int y) {
        SpriteDescriptor spriteDescriptor = renderable.getSpriteDescriptor();

        render(renderer, spriteDescriptor, x, y);
    }

    public void render(Renderer renderer, SpriteDescriptor spriteDescriptor, int x, int y) {
        List<Sprite> sprites = spriteDescriptor.sheetSelector().apply(assets);

        for (int row = 0; row < spriteDescriptor.rows(); row++) {
            for (int col = 0; col < spriteDescriptor.cols(); col++) {
                int spriteIndex = spriteDescriptor.baseIndex() + (row * spriteDescriptor.cols()) + col;
                Sprite sprite = sprites.get(spriteIndex);

                int screenX = x + spriteDescriptor.offsetX() + (col * sprite.getWidth());
                int screenY = y + spriteDescriptor.offsetY() + (row * sprite.getHeight());

                if (isOnScreen(sprite, screenX, screenY)) {
                    renderer.draw(sprite, screenX, screenY);
                }
            }
        }
    }

    // TODO should probably be a viewport method? Or at least the constants extracted somewhere
    // for now this is fine though
    private boolean isOnScreen(Sprite sprite, int screenX, int screenY) {
        return screenX + sprite.getWidth() > 16 && screenX < 240 &&
                screenY + sprite.getHeight() > 16 && screenY < 192;
    }
}

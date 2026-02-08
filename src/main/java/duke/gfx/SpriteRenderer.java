package duke.gfx;

import duke.Renderer;
import duke.resources.AssetManager;

import java.util.List;
import java.util.function.Function;

public class SpriteRenderer {
    private AssetManager assets;

    public SpriteRenderer(AssetManager assets) {
        this.assets = assets;
    }

    public void render(Renderer renderer, SpriteRenderable renderable, int x, int y) {
        if (renderable.isVisible()) {
            SpriteDescriptor spriteDescriptor = renderable.getSpriteDescriptor();

            render(renderer, spriteDescriptor, x, y);
        }
    }

    public void render(Renderer renderer, SpriteDescriptor spriteDescriptor, int x, int y) {
        List<Sprite> sprites = spriteDescriptor.sheetSelector().apply(assets);

        for (int row = 0; row < spriteDescriptor.rows(); row++) {
            for (int col = 0; col < spriteDescriptor.cols(); col++) {
                try {
                    int spriteIndex = spriteDescriptor.baseIndex() + (row * spriteDescriptor.cols()) + col;
                    Sprite sprite = sprites.get(spriteIndex);

                    int screenX = x + spriteDescriptor.offsetX() + (col * sprite.getWidth());
                    int screenY = y + spriteDescriptor.offsetY() + (row * sprite.getHeight());

                    if (isOnScreen(sprite, screenX, screenY)) {
                        renderer.draw(sprite, screenX, screenY);
                    }
                } catch (Exception e) {
                    // spriteDescriptor.baseIndex() + (row * spriteDescriptor.cols()) + col;
                    System.err.format("Failed rendering base idx %d + (%d * %d) + %d\n", spriteDescriptor.baseIndex(), row, spriteDescriptor.cols(), col);
                }
            }
        }
    }

    public void render(Renderer renderer, Function<AssetManager, List<Sprite>> sheetSelector, int index, int x, int y) {
        Sprite sprite = sheetSelector.apply(assets).get(index);

        if (isOnScreen(sprite, x, y)) {
            renderer.draw(sprite, x, y);
        }
    }

    // TODO should probably be a viewport method? Or at least the constants extracted somewhere
    // for now this is fine though
    private boolean isOnScreen(Sprite sprite, int screenX, int screenY) {
        return screenX + sprite.getWidth() > 16 && screenX < Viewport.WIDTH &&
                screenY + sprite.getHeight() > 16 && screenY < Viewport.HEIGHT;
    }
}

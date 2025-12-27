package duke.gfx;

import duke.Renderer;
import duke.gameplay.SpriteRenderable;
import duke.resources.AssetManager;

import java.util.List;

public class SpriteRenderer {
    private AssetManager assets;

    public SpriteRenderer(AssetManager assets) {
        this.assets = assets;
    }

    public void render(Renderer renderer, SpriteRenderable renderable, int x, int y) {
        Animation animation = renderable.getAnimation();
        SpriteDescriptor spriteDescriptor = animation.getSpriteDescriptor();

        List<Sprite> sprites = spriteDescriptor.sheetSelector().apply(assets);

        for (int row = 0; row < spriteDescriptor.rows(); row++) {
            for (int col = 0; col < spriteDescriptor.cols(); col++) {
                int spriteIndex = animation.getCurrentBaseIndex() + (row * spriteDescriptor.cols()) + col;
                Sprite sprite = sprites.get(spriteIndex);

                int screenX = x + spriteDescriptor.offsetX() + (col * sprite.getWidth());
                int screenY = y + spriteDescriptor.offsetY() + (row * sprite.getHeight());

                renderer.draw(sprite, screenX, screenY);
            }
        }
    }
}

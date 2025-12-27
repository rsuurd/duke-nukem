package duke.gfx;

import duke.Renderer;
import duke.resources.AssetManager;

import java.util.List;

import static duke.level.Level.TILE_SIZE;

public class AnimationRenderer {
    private AssetManager assets;

    public AnimationRenderer(AssetManager assets) {
        this.assets = assets;
    }

    public void render(Renderer renderer, Animation animation, int x, int y) {
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

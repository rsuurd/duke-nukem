package duke.gfx;

import duke.Renderer;
import duke.level.Level;
import duke.resources.AssetManager;

import static duke.level.Level.*;

public class LevelRenderer {
    private AssetManager assets;
    private Level level;
    int flasher;

    public LevelRenderer(AssetManager assets, Level level) {
        this.assets = assets;
        this.level = level;

        flasher = 0;
    }

    public void render(Renderer renderer, Viewport viewport) {
        drawBackdrop(renderer);
        drawTiles(renderer, viewport);

        flasher = (flasher + 1) % 4;
    }

    private void drawBackdrop(Renderer renderer) {
        Sprite background = assets.getBackdrop(level.getBackdrop());
        renderer.draw(background, TILE_SIZE, TILE_SIZE);
    }

    private void drawTiles(Renderer renderer, Viewport viewport) {
        int gridX = viewport.getX() / TILE_SIZE;
        int gridY = viewport.getY() / TILE_SIZE;

        for (int row = gridY; row < (gridY + 12); row++) {
            for (int col = gridX; col < (gridX + 15); col++) {
                int tileId = level.getTile(row, col);

                Sprite sprite = resolve(assets, tileId);

                if (sprite != null) {
                    int screenX = viewport.toScreenX(col * TILE_SIZE);
                    int screenY = viewport.toScreenY(row * TILE_SIZE);

                    renderer.draw(sprite, screenX, screenY);
                }
            }
        }
    }

    private Sprite resolve(AssetManager assets, int tileId) {
        if (tileId == 0x0 || tileId >= ACTIVE) {
            return null;
        }

        int spriteIndex = tileId / 32;

        if (tileId >= FLASHERS && tileId < BACKGROUNDS) {
            spriteIndex += flasher;
        }

        return assets.getTiles().get(spriteIndex);
    }
}

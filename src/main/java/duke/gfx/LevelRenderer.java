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

        setLevel(level);
    }

    public void setLevel(Level level) {
        this.level = level;
        flasher = 0;
    }

    public void render(Renderer renderer, Viewport viewport) {
        drawBackdrop(renderer, viewport);
        drawTiles(renderer, viewport);

        flasher = (flasher + 1) % 4;
    }

    private void drawBackdrop(Renderer renderer, Viewport viewport) {
        Sprite background = assets.getBackdrop(determineVisibleBackdrop(viewport));

        renderer.draw(background, TILE_SIZE, TILE_SIZE);
    }

    private int determineVisibleBackdrop(Viewport viewport) {
        int gridX = viewport.getX() / TILE_SIZE;
        int gridY = viewport.getY() / TILE_SIZE;

        for (int row = gridY; row < (gridY + ROWS); row++) {
            for (int col = gridX; col < (gridX + COLUMNS); col++) {
                int tileId = level.getTile(row, col);

                if (tileId == SECONDARY_BACKDROP_TILE_ID) {
                    return level.getDescriptor().secondaryBackdrop();
                }
            }
        }

        return level.getDescriptor().backdrop();
    }

    private void drawTiles(Renderer renderer, Viewport viewport) {
        int gridX = viewport.getX() / TILE_SIZE;
        int gridY = viewport.getY() / TILE_SIZE;

        for (int row = gridY; row < (gridY + ROWS); row++) {
            for (int col = gridX; col < (gridX + COLUMNS); col++) {
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
        if (tileId < FLASHERS || tileId >= ACTIVE) {
            return null;
        }

        int spriteIndex = tileId / 32;

        if (tileId < BACKGROUNDS) {
            spriteIndex += flasher;
        }

        return assets.getTiles().get(spriteIndex);
    }

    private static final int ROWS = 11;
    private static final int COLUMNS = 14;

    static final int SECONDARY_BACKDROP_TILE_ID = 0x20;
}

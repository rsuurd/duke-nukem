package duke.effects;

import duke.Assets;
import duke.Renderer;

import static duke.Gfx.TILE_SIZE;

public class Debris extends Effect {
    private int velocityY;

    public Debris(int x, int y) {
        super(x, y, 152, 16);

        velocityY = -2;
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        int tileIndex = 152 + ((frame / 4) * 2);

        renderer.drawTile(assets.getAnim(tileIndex), x, y);
        renderer.drawTile(assets.getAnim(tileIndex + 1), x + TILE_SIZE, y);

        frame ++;
        velocityY ++;
        y += velocityY;
    }
}

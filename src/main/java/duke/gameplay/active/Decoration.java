package duke.gameplay.active;

import duke.gameplay.Active;
import duke.gameplay.Layer;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;

public class Decoration extends Active implements SpriteRenderable {
    private SpriteDescriptor spriteDescriptor;

    public Decoration(int x, int y, SpriteDescriptor spriteDescriptor) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        this.spriteDescriptor = spriteDescriptor;
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return spriteDescriptor;
    }

    @Override
    public Layer getLayer() {
        return Layer.BACKGROUND;
    }
}

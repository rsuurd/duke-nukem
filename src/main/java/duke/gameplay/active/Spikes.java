package duke.gameplay.active;

import duke.gameplay.Active;
import duke.gameplay.Damaging;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;

public class Spikes extends Active implements SpriteRenderable, Damaging {
    private SpriteDescriptor spriteDescriptor;

    public Spikes(int x, int y, SpriteDescriptor spriteDescriptor) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        this.spriteDescriptor = spriteDescriptor;
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return spriteDescriptor;
    }
}

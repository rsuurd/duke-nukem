package duke.gameplay.active;

import duke.gameplay.Active;
import duke.gameplay.Damaging;
import duke.gameplay.GameplayContext;
import duke.gameplay.Updatable;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;

public class Needle extends Active implements SpriteRenderable, Updatable, Damaging {
    private SpriteDescriptor descriptor;

    public Needle(int x, int y) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        descriptor = RETRACTED;
    }

    @Override
    public void update(GameplayContext context) {
        descriptor = context.getPlayer().intersects(this) ? EXTENDED : RETRACTED;
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return descriptor;
    }

    private static final SpriteDescriptor RETRACTED = new SpriteDescriptor(SpriteDescriptor.OBJECTS, 95);
    private static final SpriteDescriptor EXTENDED = new SpriteDescriptor(SpriteDescriptor.OBJECTS, 96);
}

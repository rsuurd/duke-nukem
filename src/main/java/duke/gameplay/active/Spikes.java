package duke.gameplay.active;

import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Updatable;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.resources.AssetManager;

public class Spikes extends Active implements Updatable, SpriteRenderable {
    private SpriteDescriptor spriteDescriptor;

    public Spikes(int x, int y, Direction direction) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        spriteDescriptor = new SpriteDescriptor(AssetManager::getObjects, 148 + direction.ordinal(), 0, 0, 1, 1);
    }

    @Override
    public void update(GameplayContext context) {
        // if player touched, hurt
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return spriteDescriptor;
    }

    public enum Direction {
        UP, DOWN
    }
}

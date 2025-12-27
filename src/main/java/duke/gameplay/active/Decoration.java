package duke.gameplay.active;

import duke.gameplay.Active;
import duke.gameplay.SpriteRenderable;
import duke.gfx.Animation;
import duke.level.Level;

public class Decoration extends Active implements SpriteRenderable {
    private Animation animation;

    public Decoration(int x, int y, Animation animation) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }
}

package duke.gameplay.active.items;

import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Physics;
import duke.gameplay.Updatable;
import duke.gameplay.active.items.behavior.ItemBehavior;
import duke.gfx.AnimatedSpriteRenderable;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;

public class Item extends Active implements Physics, Updatable, SpriteRenderable {
    private SpriteRenderable renderable;
    private ItemBehavior behavior;

    public Item(int x, int y, SpriteRenderable renderable, ItemBehavior behavior) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        this.renderable = renderable;
        this.behavior = behavior;
    }

    @Override
    public void update(GameplayContext context) {
        if (context.getPlayer().intersects(this)) {
            behavior.pickedUp(context, this);
            destroy();
        }

        if (renderable instanceof AnimatedSpriteRenderable animated) {
            animated.tick();
        }
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return renderable.getSpriteDescriptor();
    }
}

package duke.gameplay.active.items;

import duke.gameplay.Damaging;
import duke.gameplay.active.items.behavior.ExplodingItemBehavior;
import duke.gameplay.active.items.behavior.ItemBehavior;
import duke.gfx.SimpleSpriteRenderable;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;

import static duke.gfx.SpriteDescriptor.ANIM;

public class Bomb extends Item implements Damaging {
    public Bomb(int x, int y) {
        this(x, y, new SimpleSpriteRenderable(DESCRIPTOR), new ExplodingItemBehavior());
    }

    public Bomb(int x, int y, SpriteRenderable renderable, ItemBehavior behavior) {
        super(x, y, renderable, behavior);
    }

    private static final SpriteDescriptor DESCRIPTOR = new SpriteDescriptor(ANIM, 231);
}

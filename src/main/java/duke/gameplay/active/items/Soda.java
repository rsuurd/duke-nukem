package duke.gameplay.active.items;

import duke.gameplay.Bolt;
import duke.gameplay.GameplayContext;
import duke.gameplay.Shootable;
import duke.gameplay.active.items.behavior.ItemBehavior;
import duke.gfx.SpriteRenderable;
import duke.sfx.Sfx;

public class Soda extends Item implements Shootable {
    public Soda(int x, int y, SpriteRenderable renderable, ItemBehavior behavior) {
        super(x, y, renderable, behavior);
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        destroy();

        context.getActiveManager().spawn(ItemFactory.createFizzingSoda(getX(), getY()));
        context.getSoundManager().play(Sfx.COKE_CAN_HIT);
    }
}

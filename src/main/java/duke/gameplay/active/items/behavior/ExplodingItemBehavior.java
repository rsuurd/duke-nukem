package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.active.items.Item;
import duke.gameplay.effects.Explosion;
import duke.sfx.Sfx;

public class ExplodingItemBehavior implements ItemBehavior {
    @Override
    public void pickedUp(GameplayContext context, Item item) {
        context.getActiveManager().spawn(new Explosion(item.getX(), item.getY()));
        context.getSoundManager().play(Sfx.BOMB_EXPLODE);
    }
}

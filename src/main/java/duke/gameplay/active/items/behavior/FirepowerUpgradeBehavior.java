package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.active.items.Item;
import duke.sfx.Sfx;

public class FirepowerUpgradeBehavior implements ItemBehavior {
    @Override
    public void pickedUp(GameplayContext context, Item item) {
        context.getPlayer().getWeapon().increaseFirepower();
        context.getScoreManager().score(1000, item.getX(), item.getY());
        context.getSoundManager().play(Sfx.SPECIAL_ITEM);
    }
}

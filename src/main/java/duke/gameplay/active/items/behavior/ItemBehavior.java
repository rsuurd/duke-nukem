package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.active.items.Item;

public interface ItemBehavior {
    void pickedUp(GameplayContext context, Item item);
}

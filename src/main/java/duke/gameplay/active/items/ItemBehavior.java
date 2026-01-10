package duke.gameplay.active.items;

import duke.gameplay.GameplayContext;

public interface ItemBehavior {
    void pickedUp(GameplayContext context, Item item);
}

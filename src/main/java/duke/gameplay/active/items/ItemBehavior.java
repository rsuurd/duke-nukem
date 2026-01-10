package duke.gameplay.active.items;

import duke.gameplay.GameplayContext;
import duke.gameplay.Shootable;

public interface ItemBehavior extends Shootable {
    void pickedUp(GameplayContext context, Item source);
}

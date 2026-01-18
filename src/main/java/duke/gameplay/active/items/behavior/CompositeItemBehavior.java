package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.active.items.Item;

import java.util.List;

public class CompositeItemBehavior implements ItemBehavior {
    private List<ItemBehavior> behaviors;

    public CompositeItemBehavior(ItemBehavior... behaviors) {
        this(List.of(behaviors));
    }

    public CompositeItemBehavior(List<ItemBehavior> behaviors) {
        this.behaviors = behaviors;
    }

    @Override
    public void pickedUp(GameplayContext context, Item item) {
        for (ItemBehavior behavior : behaviors) {
            behavior.pickedUp(context, item);
        }
    }
}

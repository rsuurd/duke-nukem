package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.active.items.Item;
import duke.gameplay.active.items.Key;

public class KeyBehavior implements ItemBehavior {
    private Key.Type key;

    public KeyBehavior(Key.Type key) {
        this.key = key;
    }

    @Override
    public void pickedUp(GameplayContext context, Item item) {
        context.getPlayer().getInventory().addKey(key);
    }
}

package duke.gameplay.active.items;

import duke.gameplay.GameplayContext;

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

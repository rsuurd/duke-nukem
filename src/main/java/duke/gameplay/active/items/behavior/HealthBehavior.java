package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.active.items.Item;

public class HealthBehavior implements ItemBehavior {
    private int hp;

    public HealthBehavior(int hp) {
        this.hp = hp;
    }

    @Override
    public void pickedUp(GameplayContext context, Item item) {
        context.getPlayer().getHealth().increaseHealth(hp);
    }
}

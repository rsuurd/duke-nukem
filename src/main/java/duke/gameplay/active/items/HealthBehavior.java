package duke.gameplay.active.items;

import duke.gameplay.GameplayContext;

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

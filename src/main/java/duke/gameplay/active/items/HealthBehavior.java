package duke.gameplay.active.items;

import duke.gameplay.GameplayContext;
import duke.sfx.Sfx;

public class HealthBehavior implements ItemBehavior {
    private int hp;
    private int points;

    public HealthBehavior(int hp, int points) {
        this.hp = hp;
        this.points = points;
    }

    @Override
    public void pickedUp(GameplayContext context, Item item) {
        // context.getPlayer().addHealth(hp);
        context.getScoreManager().score(points, item.getX(), item.getY());
        context.getSoundManager().play(Sfx.GET_FOOD_ITEM);
        item.destroy();
    }
}

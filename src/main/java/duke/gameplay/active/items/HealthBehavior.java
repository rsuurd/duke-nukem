package duke.gameplay.active.items;

import duke.gameplay.GameplayContext;
import duke.sfx.Sfx;

public class HealthBehavior implements ItemBehavior {
    private int hp;
    private int points;
    private Sfx sfx;

    public HealthBehavior(int hp, int points) {
        this(hp, points, Sfx.GET_FOOD_ITEM);
    }

    public HealthBehavior(int hp, int points, Sfx sfx) {
        this.hp = hp;
        this.points = points;
        this.sfx = sfx;
    }

    @Override
    public void pickedUp(GameplayContext context, Item item) {
        // context.getPlayer().addHealth(hp);
        context.getScoreManager().score(points, item.getX(), item.getY());
        context.getSoundManager().play(sfx);
        item.destroy();
    }
}

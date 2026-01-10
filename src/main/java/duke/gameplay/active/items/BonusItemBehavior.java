package duke.gameplay.active.items;

import duke.gameplay.GameplayContext;
import duke.sfx.Sfx;

public class BonusItemBehavior implements ItemBehavior {
    private int points;

    public BonusItemBehavior(int points) {
        this.points = points;
    }

    protected int determinePoints(GameplayContext context) {
        return points;
    }

    @Override
    public void pickedUp(GameplayContext context, Item item) {
        context.getScoreManager().score(determinePoints(context), item.getX(), item.getY());
        context.getSoundManager().play(Sfx.GET_BONUS_OBJECT);
        item.destroy();
    }

    public static class RandomBonusItemBehavior extends BonusItemBehavior {
        private int[] points = new int[]{100, 2000, 5000};

        public RandomBonusItemBehavior() {
            super(0);
        }

        @Override
        protected int determinePoints(GameplayContext context) {
            // actually we need to take the points based on the game tic but random is fine for now
            return points[(int) (Math.random() * points.length)];
        }
    }
}

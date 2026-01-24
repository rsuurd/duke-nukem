package duke.gameplay.active.items.behavior;

import duke.dialog.Hints;
import duke.sfx.Sfx;

public class ItemBehaviorFactory {
    public static ItemBehavior bonus(int points, Sfx sfx) {
        return of(new BonusItemBehavior(points), new SoundBehavior(sfx));
    }

    public static ItemBehavior randomBonus() {
        return of(new BonusItemBehavior.RandomBonusItemBehavior(), new SoundBehavior(Sfx.GET_BONUS_OBJECT));
    }

    public static ItemBehavior health(int hp, int points, Sfx sfx, Hints.Type hintType) {
        return of(new HealthBehavior(hp), bonus(points, sfx), new SecretTipBehavior(hintType));
    }

    public static ItemBehavior of(ItemBehavior... behaviors) {
        if (behaviors.length == 1) {
            return behaviors[0];
        } else {
            return new CompositeItemBehavior(behaviors);
        }
    }
}

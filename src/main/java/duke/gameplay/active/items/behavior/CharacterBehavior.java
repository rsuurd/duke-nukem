package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.active.items.Item;
import duke.sfx.Sfx;

public class CharacterBehavior implements ItemBehavior {
    private char c;

    public CharacterBehavior(char c) {
        this.c = c;
    }

    @Override
    public void pickedUp(GameplayContext context, Item item) {
        boolean complete = context.getBonusTracker().trackDUKE(c);
        int points = complete ? 10000 : 500;
        Sfx sound = complete ? Sfx.GET_DUKE_SOUND : Sfx.GET_BONUS_OBJECT;

        context.getScoreManager().score(points, item.getX(), item.getY());
        context.getSoundManager().play(sound);
    }
}

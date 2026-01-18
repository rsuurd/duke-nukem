package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.active.items.Item;
import duke.sfx.Sfx;

public class SoundBehavior implements ItemBehavior {
    private Sfx sfx;

    public SoundBehavior(Sfx sfx) {
        this.sfx = sfx;
    }

    @Override
    public void pickedUp(GameplayContext context, Item item) {
        context.getSoundManager().play(sfx);
    }
}

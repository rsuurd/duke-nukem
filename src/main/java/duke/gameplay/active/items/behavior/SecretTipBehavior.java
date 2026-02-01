package duke.gameplay.active.items.behavior;

import duke.dialog.Hints;
import duke.gameplay.GameplayContext;
import duke.gameplay.active.items.Item;

public class SecretTipBehavior implements ItemBehavior {
    private Hints.Hint hint;

    public SecretTipBehavior(Hints.Hint hint) {
        this.hint = hint;
    }

    @Override
    public void pickedUp(GameplayContext context, Item item) {
        context.getHints().showHint(hint, context);
    }
}

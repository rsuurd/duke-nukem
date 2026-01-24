package duke.gameplay.active.items.behavior;

import duke.dialog.Hints;
import duke.gameplay.GameplayContext;
import duke.gameplay.active.items.Item;

public class SecretTipBehavior implements ItemBehavior {
    private Hints.Type type;

    public SecretTipBehavior(Hints.Type type) {
        this.type = type;
    }

    @Override
    public void pickedUp(GameplayContext context, Item item) {
        context.getHints().showHint(type, context);
    }
}

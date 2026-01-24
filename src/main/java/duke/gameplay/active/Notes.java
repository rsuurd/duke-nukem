package duke.gameplay.active;

import duke.dialog.Dialog;
import duke.dialog.Hints;
import duke.gameplay.GameplayContext;
import duke.gameplay.Interactable;
import duke.gameplay.Updatable;
import duke.gameplay.player.Player;
import duke.gfx.SpriteDescriptor;
import duke.sfx.Sfx;

import static duke.gfx.SpriteDescriptor.OBJECTS;

public class Notes extends Decoration implements Interactable, Updatable {
    public Notes(int x, int y) {
        super(x, y, DESCRIPTOR);
    }

    @Override
    public boolean canInteract(Player player) {
        return player.intersects(this);
    }

    @Override
    public void interactRequested(GameplayContext context) {
        context.getSoundManager().play(Sfx.READ_NOTE);
        context.getDialogManager().open(Dialog.notes(context.getLevel().getDescriptor().message()));
    }

    @Override
    public void update(GameplayContext context) {
        if (canInteract(context.getPlayer())) {
            context.getHints().showHint(Hints.Type.NOTES, context);
        }
    }

    private static final SpriteDescriptor DESCRIPTOR = new SpriteDescriptor(OBJECTS, 123);
}

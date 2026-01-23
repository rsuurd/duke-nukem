package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.Interactable;
import duke.gameplay.player.Player;
import duke.gfx.SpriteDescriptor;

import static duke.gfx.SpriteDescriptor.OBJECTS;

public class Notes extends Decoration implements Interactable {
    public Notes(int x, int y) {
        super(x, y, DESCRIPTOR);
    }

    @Override
    public boolean canInteract(Player player) {
        return player.intersects(this);
    }

    @Override
    public void interactRequested(GameplayContext context) {
        // show message with text
    }

    private static final SpriteDescriptor DESCRIPTOR = new SpriteDescriptor(OBJECTS, 123);
}

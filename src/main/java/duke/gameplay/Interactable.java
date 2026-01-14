package duke.gameplay;

import duke.gameplay.player.Player;

public interface Interactable {
    boolean canInteract(Player player);

    void interactRequested(GameplayContext context);
}

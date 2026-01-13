package duke.gameplay;

public interface Interactable {
    boolean canInteract(Player player);

    void interactRequested(GameplayContext context);
}

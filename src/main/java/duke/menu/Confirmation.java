package duke.menu;

import duke.GameSystems;
import duke.dialog.Dialog;

import static java.awt.event.KeyEvent.VK_Y;

public class Confirmation implements Menu {
    private Dialog dialog;
    private Runnable onConfirm;

    public Confirmation(int x, int y, String message, Runnable onConfirm) {
        dialog = new Dialog(message, x, y, 2, 13, true, false);

        this.onConfirm = onConfirm;
    }

    @Override
    public void open(GameSystems systems) {
        systems.getDialogManager().open(dialog);
    }

    @Override
    public void update(GameSystems systems) {
        if (systems.getKeyHandler().consume(VK_Y)) {
            onConfirm.run();

            close(systems);
        } else if (systems.getKeyHandler().consumeAny()) {
            close(systems);
        }
    }

    private void close(GameSystems systems) {
        systems.getMenuManager().closeAll(systems);
    }
}

package duke.gameplay;

import duke.gameplay.active.items.Key;
import duke.gameplay.player.Inventory;
import duke.ui.KeyHandler;

import java.awt.event.KeyEvent;

public class Cheats {
    private boolean enabled;

    private boolean godUsed;
    private boolean gowUsed;

    public Cheats(boolean enabled) {
        this.enabled = enabled;

        reset();
    }

    private void reset() {
        godUsed = false;
        gowUsed = false;
    }

    public void processInput(KeyHandler handler, GameplayContext context) {
        if (!enabled) return;

        if (isPressed(handler, GOD)) {
            addAllItems(context);
        } else if (isPressed(handler, GOW)) {
            warpToNextLevel(context);
        } else {
            reset();
        }
    }

    private void addAllItems(GameplayContext context) {
        if (godUsed) return;

        for (int i = 0; i < 3; i++) {
            context.getPlayer().getWeapon().increaseFirepower();
        }

        for (Key.Type keyType : Key.Type.values()) {
            context.getPlayer().getInventory().addKey(keyType);
        }

        for (Inventory.Equipment equipment : Inventory.Equipment.values()) {
            context.getPlayer().getInventory().addEquipment(equipment);
        }

        godUsed = true;
    }

    private void warpToNextLevel(GameplayContext context) {
        if (gowUsed) return;

        context.getLevel().complete();

        gowUsed = true;
    }

    private boolean isPressed(KeyHandler handler, int[] keySequence) {
        for (int key : keySequence) {
            if (!handler.isPressed(key)) {
                return false;
            }
        }

        return true;
    }

    static final int[] GOD = {KeyEvent.VK_G, KeyEvent.VK_O, KeyEvent.VK_D};
    static final int[] GOW = {KeyEvent.VK_G, KeyEvent.VK_O, KeyEvent.VK_W};
}

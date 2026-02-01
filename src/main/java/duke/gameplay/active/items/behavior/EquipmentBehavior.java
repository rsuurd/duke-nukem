package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.active.items.Item;
import duke.gameplay.player.Inventory;
import duke.sfx.Sfx;

public class EquipmentBehavior implements ItemBehavior {
    private Inventory.Equipment equipment;

    public EquipmentBehavior(Inventory.Equipment equipment) {
        this.equipment = equipment;
    }

    @Override
    public void pickedUp(GameplayContext context, Item item) {
        context.getPlayer().getInventory().addEquipment(equipment);
        context.getScoreManager().score(1000, item.getX(), item.getY());
        context.getSoundManager().play(Sfx.SPECIAL_ITEM);
    }
}

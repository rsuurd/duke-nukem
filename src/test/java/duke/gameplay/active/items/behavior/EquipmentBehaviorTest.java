package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.player.Inventory;
import duke.sfx.Sfx;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.mockito.Mockito.*;

class EquipmentBehaviorTest {
    @ParameterizedTest
    @EnumSource(Inventory.Equipment.class)
    void shouldAddEquipmentToInventory(Inventory.Equipment equipment) {
        EquipmentBehavior behavior = new EquipmentBehavior(equipment);
        GameplayContext context = GameplayContextFixture.create();
        Inventory inventory = mock();
        when(context.getPlayer().getInventory()).thenReturn(inventory);

        behavior.pickedUp(context, mock());

        verify(inventory).addEquipment(equipment);
        verify(context.getScoreManager()).score(1000, 0, 0);
        verify(context.getSoundManager()).play(Sfx.SPECIAL_ITEM);
    }
}

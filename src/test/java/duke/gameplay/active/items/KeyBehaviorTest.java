package duke.gameplay.active.items;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.player.Inventory;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class KeyBehaviorTest {
    @Test
    void shouldAddKeyToInventory() {
        Inventory inventory = mock();
        GameplayContext context = GameplayContextFixture.create();
        when(context.getPlayer().getInventory()).thenReturn(inventory);

        new KeyBehavior(Key.Type.BLUE).pickedUp(context, mock());

        verify(inventory).addKey(Key.Type.BLUE);
    }
}

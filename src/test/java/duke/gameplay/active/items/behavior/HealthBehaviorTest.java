package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.items.Item;
import duke.gameplay.player.Health;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class HealthBehaviorTest {
    private GameplayContext context;
    private Item item;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
        item = mock();
    }

    @Test
    void shouldPickUp() {
        Health health = mock();
        when(context.getPlayer().getHealth()).thenReturn(health);
        ItemBehavior behavior = new HealthBehavior(1);

        behavior.pickedUp(context, item);

        verify(context.getPlayer().getHealth()).increaseHealth(1);
    }
}

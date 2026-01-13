package duke.gameplay.active.items;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        ItemBehavior behavior = new HealthBehavior(1);

        behavior.pickedUp(context, item);

        verify(context.getPlayer()).increaseHealth(1);
    }
}

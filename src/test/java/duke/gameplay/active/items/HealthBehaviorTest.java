package duke.gameplay.active.items;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.sfx.Sfx;
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
        ItemBehavior behavior = new HealthBehavior(1, 100);

        behavior.pickedUp(context, item);

        verify(context.getPlayer()).increaseHealth(1);
        verify(context.getSoundManager()).play(Sfx.GET_FOOD_ITEM);
        verify(context.getScoreManager()).score(100, item.getX(), item.getY());
        verify(item).destroy();
    }
}

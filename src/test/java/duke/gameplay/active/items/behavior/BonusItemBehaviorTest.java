package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.items.Item;
import duke.gameplay.active.items.behavior.BonusItemBehavior.RandomBonusItemBehavior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BonusItemBehaviorTest {
    private GameplayContext context;
    private Item item;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
        item = mock();
    }

    @Test
    void shouldPickUp() {
        ItemBehavior behavior = new BonusItemBehavior(100);

        behavior.pickedUp(context, item);

        verify(context.getScoreManager()).score(100, item.getX(), item.getY());
    }

    @Test
    void shouldAwardRandomBonusPoints() {
        ItemBehavior behavior = new RandomBonusItemBehavior();

        behavior.pickedUp(context, item);

        verify(context.getScoreManager()).score(anyInt(), anyInt(), anyInt());
    }
}

package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.items.Item;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CompositeItemBehaviorTest {
    @Test
    void shouldRunBehaviors() {
        ItemBehavior addPoints = mock();
        ItemBehavior playSound = mock();

        ItemBehavior behavior = new CompositeItemBehavior(addPoints, playSound);

        GameplayContext context = GameplayContextFixture.create();
        Item item = mock();
        behavior.pickedUp(context, item);

        verify(addPoints).pickedUp(context, item);
        verify(playSound).pickedUp(context, item);
    }
}

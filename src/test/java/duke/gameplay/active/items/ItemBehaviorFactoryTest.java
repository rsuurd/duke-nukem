package duke.gameplay.active.items;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ItemBehaviorFactoryTest {
    @Test
    void shouldCreateSingleBehavior() {
        ItemBehavior behavior = mock();

        assertThat(ItemBehaviorFactory.of(behavior)).isSameAs(behavior);
    }

    @Test
    void shouldCreateCompositeBehavior() {
        ItemBehavior addPoints = mock();
        ItemBehavior playSound = mock();

        ItemBehavior behavior = ItemBehaviorFactory.of(addPoints, playSound);

        assertThat(behavior).isInstanceOf(CompositeItemBehavior.class);
    }
}
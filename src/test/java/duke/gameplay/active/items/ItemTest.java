package duke.gameplay.active.items;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gfx.AnimatedSpriteRenderable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ItemTest {
    private Item item;
    private AnimatedSpriteRenderable renderable;
    private ItemBehavior behavior;

    private GameplayContext context;

    @BeforeEach
    void createItem() {
        behavior = mock();
        renderable = mock();
        item = new Item(0, 0, renderable, behavior);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldPickUpWhenTouching() {
        when(context.getPlayer().intersects(item)).thenReturn(true);

        item.update(context);

        verify(behavior).pickedUp(context, item);
    }

    @Test
    void shouldNotPickUpWhenNotTouching() {
        when(context.getPlayer().intersects(item)).thenReturn(false);

        item.update(context);

        verifyNoInteractions(behavior);
    }

    @Test
    void shouldAnimateItem() {
        item.update(context);

        verify(renderable).tick();
    }

}

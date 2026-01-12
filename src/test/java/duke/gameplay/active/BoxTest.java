package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.items.Item;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BoxTest {
    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBeShot() {
        Item contents = mock();

        Box box = new Box(0, 0, mock(), (x, y) -> contents);

        box.onShot(context, mock());

        assertThat(box.isActivated()).isFalse();

        verify(context.getActiveManager()).spawn(contents);
        verify(context.getActiveManager()).spawn(anyList());
        verify(context.getSoundManager()).play(Sfx.BOX_EXPLODE);
    }
}

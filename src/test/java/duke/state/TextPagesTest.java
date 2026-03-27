package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TextPagesTest {
    private GameState next;

    private TextPages text;

    private GameSystems systems;

    @BeforeEach
    void create() {
        next = mock();

        text = new TextPages("text", next);

        systems = GameSystemsFixture.create();

        when(systems.getAssets().getTiles()).thenReturn(mock());
        when(systems.getAssets().getObjects()).thenReturn(mock());
    }

    @Test
    void shouldLoadResources() {
        text.start(systems);
    }

    @Test
    void shouldAdvancePageThenTransitionToNext() {
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        text.start(systems);
        text.update(systems);
        text.update(systems);

        verify(systems.getStateRequester()).requestState(next);
    }
}

package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TextPagesTest {
    private TextPages text;

    private GameSystems systems;

    @BeforeEach
    void create() {
        text = new TextPages("text");

        systems = GameSystemsFixture.create();

        when(systems.getAssets().getTiles()).thenReturn(mock());
        when(systems.getAssets().getObjects()).thenReturn(mock());
    }

    @Test
    void shouldLoadResources() {
        text.start(systems);
    }

    @Test
    void shouldAdvancePageThenBackToTitle() {
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        text.start(systems);
        text.update(systems);
        text.update(systems);

        verify(systems.getStateRequester()).requestState(isA(TitleScreen.class));
    }
}

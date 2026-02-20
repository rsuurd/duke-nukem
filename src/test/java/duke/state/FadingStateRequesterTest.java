package duke.state;

import duke.GameSystems;
import duke.gfx.EgaPalette;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FadingStateRequesterTest {
    @Mock
    private StateManager stateManager;

    @Mock
    private EgaPalette palette;

    @InjectMocks
    private FadingStateRequester requester;

    @Mock
    private GameState next;

    @Mock
    private GameSystems systems;

    @Test
    void shouldChangeStateWithoutTransition() {
        requester.requestState(next, StateRequester.Transition.NONE);

        verifyNoInteractions(palette);

        requester.update(systems);

        verify(stateManager).set(next, systems);
    }

    @Test
    void shouldFadeToBlackBetweenStateChanges() {
        requester.requestState(next);

        verify(palette).fadeOut();

        when(palette.isFadedBack()).thenReturn(true);

        requester.update(systems);

        verify(stateManager).set(next, systems);
        verify(palette).fadeIn();
    }

    @Test
    void shouldFadeToWhiteBetweenStateChanges() {
        requester.requestState(next, StateRequester.Transition.FADE_TO_WHITE);

        verify(palette).fadeToWhite();

        when(palette.isFadedWhite()).thenReturn(true);

        requester.update(systems);

        verify(stateManager).set(next, systems);
        verify(palette).fadeFromWhite();
    }

    @Test
    void shouldNotAllowMultipleStateChangeRequests() {
        assertThatThrownBy(() -> {
            requester.requestState(next);
            requester.requestState(next);
        }).isInstanceOf(IllegalStateException.class).hasMessage("Already requesting a state change");
    }
}

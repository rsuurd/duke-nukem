package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static duke.state.Introduction.COUNTDOWN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IntroductionTest {
    private GameSystems systems;

    private Introduction introduction;

    @BeforeEach
    void createSystems() {
        systems = GameSystemsFixture.create();
        introduction = new Introduction();
        introduction.start(systems);
    }

    @Test
    void shouldStartBlackedOut() {
        verify(systems.getPalette()).blackout();
    }

    @Test
    void shouldShowInitialDialog() {
        when(systems.getPalette().isFadedBack()).thenReturn(true);

        introduction.update(systems);

        verify(systems.getPalette()).fadeIn();
        verify(systems.getDialogManager()).open(any());
    }

    @Test
    void shouldFadeOutOnceCountDownReachesZero() {
        when(systems.getPalette().isFadedBack()).thenReturn(true, false);
        when(systems.getPalette().isFadedIn()).thenReturn(true);

        for (int i = COUNTDOWN; i >= 0; i--) {
            introduction.update(systems);
        }

        verify(systems.getPalette()).fadeOut();
    }

    @Test
    void shouldShowSecondDialog() {
        when(systems.getPalette().isFadedBack()).thenReturn(true);

        introduction.update(systems);
        reset(systems.getDialogManager());

        introduction.update(systems);

        verify(systems.getDialogManager()).open(any());
    }

    @Test
    void shouldTransitionToTitleScreen() {
        when(systems.getPalette().isFadedBack()).thenReturn(true);

        introduction.update(systems);
        introduction.update(systems);
        introduction.update(systems);

        verify(systems.getStateRequester()).requestState(isA(TitleScreen.class));
    }
}

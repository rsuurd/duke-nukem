package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gfx.Animation;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.Layer.BACKGROUND;
import static duke.gameplay.Layer.FOREGROUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExitTest {
    private GameplayContext context;

    @Mock
    private Animation animation;

    private Exit exit;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    private Exit createExit(Exit.State state) {
        return new Exit(0, 0, state, animation);
    }

    @Test
    void shouldBeInteractable() {
        exit = createExit(Exit.State.CLOSED);
        when(context.getPlayer().intersects(exit)).thenReturn(true);

        assertThat(exit.canInteract(context.getPlayer())).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = Exit.State.class, names = {"CLOSED"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldNotBeInteractable(Exit.State state) {
        exit = createExit(state);

        assertThat(exit.canInteract(context.getPlayer())).isFalse();
    }

    @Test
    void shouldBeInteractableWhenNotThere() {
        exit = createExit(Exit.State.CLOSED);
        when(context.getPlayer().intersects(exit)).thenReturn(false);

        assertThat(exit.canInteract(context.getPlayer())).isFalse();
    }

    @Test
    void shouldOpenOnInteractRequested() {
        exit = createExit(Exit.State.CLOSED);

        exit.interactRequested(context);

        assertThat(exit.getState()).isEqualTo(Exit.State.OPENING);
        verify(context.getPlayer()).disableControls();
        verify(context.getSoundManager()).play(Sfx.LEVEL_DONE);
    }

    @Test
    void shouldIdle() {
        exit = createExit(Exit.State.CLOSED);

        exit.update(context);

        verifyNoInteractions(animation);
    }

    @Test
    void shouldOpen() {
        exit = createExit(Exit.State.OPENING);

        exit.update(context);

        verify(animation).tick();
        assertThat(exit.getLayer()).isEqualTo(BACKGROUND);
    }

    @Test
    void shouldCloseWhenFullyOpen() {
        exit = createExit(Exit.State.OPENING);
        when(animation.isFinished()).thenReturn(true);

        exit.update(context);

        assertThat(exit.getState()).isEqualTo(Exit.State.CLOSING);
        assertThat(exit.getLayer()).isEqualTo(FOREGROUND);
        verify(animation).reverse();
    }

    @Test
    void shouldClose() {
        exit = createExit(Exit.State.CLOSING);

        exit.update(context);

        verify(animation).tick();
        assertThat(exit.getLayer()).isEqualTo(FOREGROUND);
    }

    @Test
    void shouldExitWhenFullyClosed() {
        exit = createExit(Exit.State.CLOSING);
        when(animation.isFinished()).thenReturn(true);

        exit.update(context);

        assertThat(exit.getState()).isEqualTo(Exit.State.EXITING);
        assertThat(exit.getLayer()).isEqualTo(FOREGROUND);
    }

    @Test
    void shouldExit() {
        exit = createExit(Exit.State.EXITING);

        exit.update(context);

        // verify(context).exited(); or something
    }
}
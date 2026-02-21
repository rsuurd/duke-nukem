package duke.state.storyboard;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.gfx.Sprite;
import duke.sfx.Sfx;
import duke.state.GameState;
import duke.state.StateRequester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoryboardHandlerTest {
    @Mock
    private Storyboard storyboard;

    @Mock
    private GameState next;

    private StoryboardHandler handler;

    private GameSystems systems;

    @BeforeEach
    void create() {
        handler = new StoryboardHandler(storyboard, next, StateRequester.Transition.FADE_TO_WHITE);

        systems = GameSystemsFixture.create();
    }

    @Test
    void shouldRenderPanel() {
        Sprite image = mock();
        when(storyboard.current()).thenReturn(new Panel(image, mock(Dialog.class)));

        handler.render(systems);

        verify(systems.getRenderer()).draw(image, 0, 0);
        verify(systems.getDialogManager()).render(systems.getRenderer());
    }

    @Test
    void shouldPlaySfxWhenPanelStarts() {
        when(storyboard.current()).thenReturn(new Panel(mock(Sprite.class), Sfx.CHEAT_MODE, mock(Dialog.class)));

        handler.update(systems);

        verify(systems.getSoundManager()).play(Sfx.CHEAT_MODE);
    }

    @Test
    void shouldFadeIn() {
        when(systems.getPalette().isFadedIn()).thenReturn(false);
        when(storyboard.current()).thenReturn(mock(Panel.class));

        handler.update(systems); // begin
        handler.update(systems); // fade in

        verifyNoInteractions(systems.getDialogManager(), systems.getStateRequester(), systems.getKeyHandler());
    }

    @Test
    void shouldOpenDialogOnceFadedIn() {
        when(systems.getPalette().isFadedIn()).thenReturn(true);

        Dialog dialog = mock();
        when(storyboard.current()).thenReturn(new Panel(mock(Sprite.class), dialog));

        handler.update(systems); // begin
        handler.update(systems); // fade in

        verify(systems.getDialogManager()).open(dialog);
    }

    @Test
    void shouldFadeOutWhenKeyPressedAndStoryboardHasNext() {
        when(systems.getPalette().isFadedIn()).thenReturn(true);
        when(storyboard.current()).thenReturn(new Panel(mock(Sprite.class), mock(Dialog.class)));
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);
        when(storyboard.hasNext()).thenReturn(true);

        handler.update(systems); // begin
        handler.update(systems); // faded in
        handler.update(systems); // keypress

        verify(systems.getPalette()).fadeOut();
    }

    @Test
    void shouldAdvanceStoryOnceFadedOut() {
        when(systems.getPalette().isFadedIn()).thenReturn(true);
        when(storyboard.current()).thenReturn(new Panel(mock(Sprite.class), mock(Dialog.class)));
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);
        when(storyboard.hasNext()).thenReturn(true);
        when(systems.getPalette().isFadedBlack()).thenReturn(true);

        handler.update(systems); // begin
        handler.update(systems); // faded in
        handler.update(systems); // keypress
        handler.update(systems); // faded out

        verify(storyboard).advance();
        verify(systems.getDialogManager()).close();
        verify(systems.getPalette()).fadeIn();
    }

    @Test
    void shouldTransitionWhenKeyPressedAndStoryIsFinished() {
        when(systems.getPalette().isFadedIn()).thenReturn(true);
        when(storyboard.current()).thenReturn(new Panel(mock(Sprite.class), mock(Dialog.class)));
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);
        when(storyboard.hasNext()).thenReturn(false);

        handler.update(systems); // begin
        handler.update(systems); // faded in
        handler.update(systems); // keypress

        verify(systems.getStateRequester()).requestState(next, StateRequester.Transition.FADE_TO_WHITE);
    }
}
package duke.gameplay.active;

import duke.dialog.Dialog;
import duke.dialog.Hints;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.level.LevelDescriptor;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotesTest {
    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBeInteractable() {
        Notes notes = new Notes(0, 0);

        assertThat(notes.canInteract(context.getPlayer())).isFalse();

        when(context.getPlayer().intersects(notes)).thenReturn(true);

        assertThat(notes.canInteract(context.getPlayer())).isTrue();
    }

    @Test
    void shouldShowNotes() {
        Notes notes = new Notes(0, 0);

        when(context.getLevel().getDescriptor()).thenReturn(new LevelDescriptor(1, 1, "Top secret"));

        notes.interactRequested(context);

        verify(context.getSoundManager()).play(Sfx.READ_NOTE);
        verify(context.getDialogManager()).open(Dialog.notes("Top secret"));
    }

    @Test
    void shouldShowHint() {
        Notes notes = new Notes(0, 0);
        when(context.getPlayer().intersects(notes)).thenReturn(true);

        notes.update(context);

        verify(context.getHints()).showHint(Hints.Hint.NOTES, context);
    }
}

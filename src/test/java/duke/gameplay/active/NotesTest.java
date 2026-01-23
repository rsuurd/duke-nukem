package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
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
}
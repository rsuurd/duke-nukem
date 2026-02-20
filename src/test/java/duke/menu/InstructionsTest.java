package duke.menu;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InstructionsTest {
    private Instructions instructions;

    private GameSystems systems;

    @BeforeEach
    void createInstructions() {
        instructions = new Instructions();

        systems = GameSystemsFixture.create();
    }

    @Test
    void shouldOpen() {
        instructions.open(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldClose() {
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        instructions.update(systems);

        verify(systems.getMenuManager()).closeAll(systems);
    }
}

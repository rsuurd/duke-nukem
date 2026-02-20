package duke.menu;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.level.LevelDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SaveGameTest {
    private GameSystems systems;
    private GameplayContext context;
    private SaveGame saveGame;

    @BeforeEach
    void create() {
        systems = GameSystemsFixture.create();
        context = GameplayContextFixture.create();

        saveGame = new SaveGame(context);
    }

    @Test
    void shouldOpen() {
        when(context.getLevel().getDescriptor()).thenReturn(new LevelDescriptor(1, 1));

        saveGame.open(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldClose() {
        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        saveGame.update(systems);

        verify(systems.getMenuManager()).closeAll(systems);
    }
}

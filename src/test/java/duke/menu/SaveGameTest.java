package duke.menu;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.SaveGameFactory;
import duke.level.LevelDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static java.awt.event.KeyEvent.VK_ENTER;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class SaveGameTest {
    private GameSystems systems;
    private GameplayContext context;
    private SaveGameFactory factory;

    private SaveGame saveGame;

    @BeforeEach
    void create() {
        systems = GameSystemsFixture.create();
        context = GameplayContextFixture.create();
        factory = mock();

        saveGame = new SaveGame(context, factory);
    }

    @Test
    void shouldRequireHallway() {
        when(context.getLevel().getDescriptor()).thenReturn(new LevelDescriptor(1, 1, 1));

        saveGame.open(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldClose() {
        when(context.getLevel().getDescriptor()).thenReturn(new LevelDescriptor(1, 1, 1));

        when(systems.getKeyHandler().consumeAny()).thenReturn(true);

        saveGame.update(systems);

        verify(systems.getMenuManager()).closeAll(systems);
    }

    @ParameterizedTest
    @ValueSource(chars = {'1', '2', '3', '4', '5', '6', '7', '8', '9'})
    void shouldSaveGame(char slot) {
        when(context.getLevel().getDescriptor()).thenReturn(new LevelDescriptor(1, 2, 0));
        when(systems.getKeyHandler().consume()).thenReturn((int) slot);
        duke.gameplay.SaveGame savedGame = mock();
        when(factory.create(context)).thenReturn(savedGame);

        saveGame.update(systems);

        verify(systems.getAssets()).saveGame(savedGame, slot);
        verify(systems.getMenuManager()).closeAll(systems);
    }

    @Test
    void shouldRejectInvalidSlot() {
        when(context.getLevel().getDescriptor()).thenReturn(new LevelDescriptor(1, 2, 0));

        saveGame.update(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
        verify(systems.getAssets(), never()).saveGame(any(), anyChar());
    }

    @Test
    void shouldCloseErrorDialogOnEnter() {
        when(context.getLevel().getDescriptor()).thenReturn(new LevelDescriptor(1, 2, 0));

        saveGame.update(systems);

        when(systems.getKeyHandler().consume()).thenReturn(VK_ENTER);

        saveGame.update(systems);

        verify(systems.getMenuManager()).closeAll(systems);
    }
}

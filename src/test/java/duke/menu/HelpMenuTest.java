package duke.menu;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.awt.event.KeyEvent.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HelpMenuTest {
    private GameSystems systems;
    private GameplayContext context;
    private HelpMenu helpMenu;

    @BeforeEach
    void create() {
        systems = GameSystemsFixture.create();
        context = GameplayContextFixture.create();

        helpMenu = new HelpMenu(context);
    }

    @Test
    void shouldOpen() {
        helpMenu.open(systems);

        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @ParameterizedTest
    @MethodSource("menuOptions")
    void shouldOpenSelectedMenu(int key, Class<? extends Menu> menu) {
        helpMenu.open(systems);

        when(systems.getKeyHandler().consume(key)).thenReturn(true);

        helpMenu.update(systems);

        verify(systems.getMenuManager()).open(isA(menu), eq(systems));
    }

    static Stream<Arguments> menuOptions() {
        return Stream.of(
                Arguments.of(VK_S, SaveGame.class),
                Arguments.of(VK_I, Instructions.class),
                Arguments.of(VK_G, GameSetup.class),
                Arguments.of(VK_H, HighScores.class),
                Arguments.of(VK_F10, Confirmation.class)
        );
    }
}

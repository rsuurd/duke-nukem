package duke.menu;

import duke.GameSystems;
import duke.dialog.DialogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuManagerTest {
    @Mock
    private GameSystems systems;

    private MenuManager manager;

    @BeforeEach
    void createManager() {
        manager = new MenuManager();
    }

    @Test
    void shouldStartEmpty() {
        assertThat(manager.isOpen()).isFalse();
        assertThat(manager.current()).isNull();
    }

    @Test
    void shouldOpenMenu() {
        Menu menu = mock();

        manager.open(menu, systems);

        assertThat(manager.isOpen()).isTrue();
        assertThat(manager.current()).isSameAs(menu);
    }

    @Test
    void shouldUpdateOpenMenu() {
        Menu menu = mock();

        manager.open(menu, systems);
        manager.update(systems);

        verify(menu).update(systems);
    }

    @Test
    void shouldCloseAllMenus() {
        DialogManager dialogManager = mock();
        when(systems.getDialogManager()).thenReturn(dialogManager);

        manager.open(mock(), systems);
        manager.closeAll(systems);

        assertThat(manager.isOpen()).isFalse();
        verify(dialogManager).close();
    }
}

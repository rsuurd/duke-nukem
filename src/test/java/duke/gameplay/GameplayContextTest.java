package duke.gameplay;

import duke.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GameplayContextTest {
    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldIndicateSolidTile() {
        when(context.getLevel().isSolid(0, 0)).thenReturn(true);

        assertThat(context.isSolid(0, 0)).isTrue();
    }

    @Test
    void shouldIndicateCellOccupiedBySolidActive() {
        when(context.getActiveManager().getActives()).thenReturn(List.of(new SolidActive()));

        assertThat(context.isSolid(0, 0)).isTrue();
    }

    @Test
    void shouldSwitchLevel() {
        when(context.getPlayer().getHealth()).thenReturn(mock());
        Level newLevel = mock();
        when(newLevel.getActives()).thenReturn(List.of(mock(Active.class)));

        context.switchLevel(newLevel);

        assertThat(context.getLevel()).isEqualTo(newLevel);
        verify(context.getActiveManager()).reset();
        verify(context.getBoltManager()).reset();
        verify(context.getActiveManager()).spawn(any(Active.class));
        verify(context.getPlayer()).setX(0);
        verify(context.getPlayer()).setY(0);
        verify(context.getPlayer()).enableControls();
        verify(context.getPlayer().getHealth()).grantInvulnerability();
    }

    private static class SolidActive extends Active implements Solid {
        private SolidActive() {
            super(0, 0, Level.TILE_SIZE, Level.TILE_SIZE);
        }
    }
}
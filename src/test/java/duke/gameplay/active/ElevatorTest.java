package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ElevatorTest {
    private Elevator elevator;

    private GameplayContext context;

    @BeforeEach
    void createElevator() {
        elevator = new Elevator(160, 160);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldMoveUp() {
        when(context.getLevel().isSolid(anyInt(), anyInt())).thenReturn(false);
        setupPlayer(160, true);

        elevator.update(context);

        assertThat(elevator.getX()).isEqualTo(160);
        assertThat(elevator.getY()).isEqualTo(144);
        verify(context.getPlayer()).setY(112);
    }

    @Test
    void shouldMoveDown() {
        when(context.getLevel().isSolid(anyInt(), anyInt())).thenReturn(false);
        setupPlayer(160, true);
        elevator.update(context);
        setupPlayer(140, true);

        elevator.update(context);

        assertThat(elevator.getY()).isEqualTo(160);
    }

    @Test
    void shouldNotMoveUpWhenNotUsing() {
        when(context.getLevel().isSolid(anyInt(), anyInt())).thenReturn(false);
        setupPlayer(160, false);

        elevator.update(context);

        assertThat(elevator.getX()).isEqualTo(160);
        assertThat(elevator.getY()).isEqualTo(160);
        verify(context.getPlayer(), never()).setY(anyInt());
    }

    @Test
    void shouldNotMoveUpWhenNotOnTop() {
        when(context.getLevel().isSolid(anyInt(), anyInt())).thenReturn(false);
        setupPlayer(100, true);

        elevator.update(context);

        assertThat(elevator.getX()).isEqualTo(160);
        assertThat(elevator.getY()).isEqualTo(160);
        verify(context.getPlayer(), never()).setY(anyInt());
    }

    @Test
    void shouldNotMoveUpWhenRiderCollides() {
        when(context.getLevel().isSolid(anyInt(), anyInt())).thenReturn(true);
        setupPlayer(160, true);

        elevator.update(context);

        assertThat(elevator.getX()).isEqualTo(160);
        assertThat(elevator.getY()).isEqualTo(160);
        verify(context.getPlayer(), never()).setY(anyInt());
    }

    private void setupPlayer(int x, boolean isUsing) {
        when(context.getPlayer().getX()).thenReturn(x);
        when(context.getPlayer().getY()).thenReturn(128);
        when(context.getPlayer().getWidth()).thenReturn(16);
        when(context.getPlayer().getHeight()).thenReturn(32);
        when(context.getPlayer().isUsing()).thenReturn(isUsing);
    }
}
package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.Player;
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
    void shouldOnlyBeInteractableWhenOnTop() {
        setupPlayer(160);

        assertThat(elevator.canInteract(context.getPlayer())).isTrue();
    }

    @Test
    void shouldMoveUpWhenInteracting() {
        when(context.getLevel().isSolid(anyInt(), anyInt())).thenReturn(false);
        setupPlayer(160);

        elevator.interactRequested(context);

        assertThat(elevator.getX()).isEqualTo(160);
        assertThat(elevator.getY()).isEqualTo(144);
        verify(context.getPlayer()).setY(112);
    }

    @Test
    void shouldNotMoveUpWhenRiderCollides() {
        when(context.getLevel().isSolid(anyInt(), anyInt())).thenReturn(true);
        setupPlayer(160);

        elevator.interactRequested(context);

        assertThat(elevator.getX()).isEqualTo(160);
        assertThat(elevator.getY()).isEqualTo(160);
        verify(context.getPlayer(), never()).setY(anyInt());
    }

    @Test
    void shouldMoveDownWhenLeaving() {
        when(context.getLevel().isSolid(anyInt(), anyInt())).thenReturn(false);

        setupPlayer(160);
        elevator.interactRequested(context);

        setupPlayer(140);
        elevator.update(context);

        assertThat(elevator.getY()).isEqualTo(160);
    }

    private void setupPlayer(int x) {
        Player player = context.getPlayer();
        when(player.getX()).thenReturn(x);
        when(player.getY()).thenReturn(128);
        when(player.getWidth()).thenReturn(16);
        when(player.getHeight()).thenReturn(32);
    }
}

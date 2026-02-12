package duke.gameplay.active;

import duke.dialog.Hints;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.player.Player;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static duke.gameplay.GameplayContextFixture.SOLID_TILE_FLAG;
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
        when(context.getLevel().getTileFlags(anyInt(), anyInt())).thenReturn(0);

        setupPlayer(160);

        elevator.interactRequested(context);

        assertThat(elevator.getX()).isEqualTo(160);
        assertThat(elevator.getY()).isEqualTo(144);
        verify(context.getPlayer()).setY(112);
        verify(context.getSoundManager()).play(Sfx.ELEVATOR);
    }

    @Test
    void shouldNotMoveUpWhenRiderCollides() {
        when(context.getLevel().getTileFlags(anyInt(), anyInt())).thenReturn(SOLID_TILE_FLAG);

        setupPlayer(160);

        elevator.interactRequested(context);

        assertThat(elevator.getX()).isEqualTo(160);
        assertThat(elevator.getY()).isEqualTo(160);
        verify(context.getPlayer(), never()).setY(anyInt());
    }

    @Test
    void shouldMoveDownWhenLeaving() {
        when(context.getLevel().getTileFlags(anyInt(), anyInt())).thenReturn(0);

        setupPlayer(160);
        elevator.interactRequested(context);

        setupPlayer(140);
        elevator.update(context);

        assertThat(elevator.getY()).isEqualTo(160);
    }

    @Test
    void shouldShowHint() {
        setupPlayer(160);

        elevator.update(context);

        verify(context.getHints()).showHint(Hints.Hint.ELEVATOR, context);
    }

    private void setupPlayer(int x) {
        Player player = context.getPlayer();
        when(player.getX()).thenReturn(x);
        when(player.getY()).thenReturn(128);
        when(player.getWidth()).thenReturn(16);
        when(player.getHeight()).thenReturn(32);
    }
}

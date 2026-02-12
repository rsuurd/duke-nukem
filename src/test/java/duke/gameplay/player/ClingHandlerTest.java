package duke.gameplay.player;

import duke.gameplay.WorldQuery;
import duke.level.Flags;
import duke.ui.KeyHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClingHandlerTest {
    private ClingHandler clingHandler = new ClingHandler();

    @Mock
    private Player player;

    @Mock
    private Inventory inventory;

    @Mock
    private WorldQuery worldQuery;

    @Mock
    private PlayerHealth health;

    @Mock
    private KeyHandler.Input input;

    @Test
    void shouldClingWhenBumpingClingableTileAndIsEquippedWithGrapplingHooks() {
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.getInventory()).thenReturn(inventory);

        clingHandler.onBump(player, Flags.CLINGABLE.bit());

        verify(player).cling();
    }

    @Test
    void shouldNotClingWhenNotEquippedWithGrapplingHooks() {
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(false);
        when(player.getInventory()).thenReturn(inventory);

        clingHandler.onBump(player, Flags.CLINGABLE.bit());

        verify(player, never()).cling();
    }

    @Test
    void shouldNotClingWhenBumpingNonClingableTileAndIsEquippedWithGrapplingHooks() {
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.getInventory()).thenReturn(inventory);

        clingHandler.onBump(player, Flags.SOLID.bit());

        verify(player, never()).cling();
    }

    @Test
    void shouldReleaseWhenTileAboveIsNotClingable() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getState()).thenReturn(State.CLINGING);
        when(player.getHealth()).thenReturn(health);

        when(worldQuery.getTileFlags(anyInt(), anyInt())).thenReturn(0);

        clingHandler.update(worldQuery, input);

        verify(player).releaseCling();
    }

    @Test
    void shouldKeepClinged() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getState()).thenReturn(State.CLINGING);
        when(input.down()).thenReturn(false);
        when(player.getHealth()).thenReturn(health);
        when(health.isDamageTaken()).thenReturn(false);
        when(worldQuery.getTileFlags(anyInt(), anyInt())).thenReturn(Flags.CLINGABLE.bit());

        clingHandler.update(worldQuery, input);

        verify(player, never()).releaseCling();
    }

    @Test
    void shouldNotReleaseWhenNotClinging() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getState()).thenReturn(State.WALKING);

        clingHandler.update(worldQuery, input);

        verify(worldQuery, never()).getTileFlags(anyInt(), anyInt());

        verify(player, never()).releaseCling();
    }

    @Test
    void shouldReleaseWhenDownIsPressed() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getState()).thenReturn(State.CLINGING);
        when(input.down()).thenReturn(true);

        clingHandler.update(worldQuery, input);

        verify(player).releaseCling();
    }

    @Test
    void shouldReleaseWhenDamageTaken() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getState()).thenReturn(State.CLINGING);
        when(input.down()).thenReturn(false);
        when(player.getHealth()).thenReturn(health);
        when(health.isDamageTaken()).thenReturn(true);

        clingHandler.update(worldQuery, input);

        verify(player).releaseCling();
    }
}
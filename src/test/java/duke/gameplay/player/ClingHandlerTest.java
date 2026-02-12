package duke.gameplay.player;

import duke.gameplay.WorldQuery;
import duke.level.Flags;
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
        when(worldQuery.getTileFlags(anyInt(), anyInt())).thenReturn(0);

        clingHandler.update(worldQuery);

        verify(player).releaseCling();
    }

    @Test
    void shouldNotReleaseWhileTileAboveIsClingable() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getState()).thenReturn(State.CLINGING);
        when(worldQuery.getTileFlags(anyInt(), anyInt())).thenReturn(Flags.CLINGABLE.bit());

        clingHandler.update(worldQuery);

        verify(player, never()).releaseCling();
    }

    @Test
    void shouldNotReleaseWhenNotClinging() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getState()).thenReturn(State.WALKING);

        clingHandler.update(worldQuery);

        verify(worldQuery, never()).getTileFlags(anyInt(), anyInt());

        verify(player, never()).releaseCling();
    }
}
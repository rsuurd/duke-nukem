package duke.gameplay.player;

import duke.gameplay.WorldQuery;
import duke.level.Flags;
import duke.ui.KeyHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrapplingHooksTest {
    private GrapplingHooks grapplingHooks = new GrapplingHooks();

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
    void shouldClingToTileAbove() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(worldQuery.getTileFlags(anyInt(), anyInt())).thenReturn(Flags.CLINGABLE.bit());

        grapplingHooks.update(worldQuery, input);

        verify(player).cling();
    }

    @Test
    void shouldNotClingWhenGrounded() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.isGrounded()).thenReturn(true);

        grapplingHooks.update(worldQuery, input);

        verify(player, never()).cling();
    }

    @Test
    void shouldNotClingWhenAlreadyClinging() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getInventory()).thenReturn(inventory);
        when(player.getHealth()).thenReturn(health);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.getState()).thenReturn(State.CLINGING);

        grapplingHooks.update(worldQuery, input);

        verify(player, never()).cling();
    }

    @Test
    void shouldNotClingToTileAboveWhenNotEquippedWithGrapplingHooks() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(false);
        when(player.getInventory()).thenReturn(inventory);

        grapplingHooks.update(worldQuery, input);

        verify(player, never()).cling();
    }

    @Test
    void shouldNotClingToTileAboveWhenNotClingable() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.getInventory()).thenReturn(inventory);
        when(worldQuery.getTileFlags(anyInt(), anyInt())).thenReturn(Flags.SOLID.bit());

        grapplingHooks.update(worldQuery, input);

        verify(player, never()).cling();
    }
    @Test
    void shouldNotClingToTileAboveWhenNotReadyToCling() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(worldQuery.getTileFlags(anyInt(), anyInt())).thenReturn(Flags.CLINGABLE.bit());

        grapplingHooks.update(worldQuery, input);
        grapplingHooks.update(worldQuery, input);

        verify(player).cling();
    }

    @Test
    void shouldReleaseWhenTileAboveIsNotClingable() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.getInventory()).thenReturn(inventory);
        when(player.getState()).thenReturn(State.CLINGING);
        when(player.getHealth()).thenReturn(health);
        when(worldQuery.getTileFlags(anyInt(), anyInt())).thenReturn(Flags.SOLID.bit());

        grapplingHooks.update(worldQuery, input);

        verify(player).releaseCling();
    }

    @Test
    void shouldKeepClinged() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.getInventory()).thenReturn(inventory);
        when(player.getHealth()).thenReturn(health);
        when(player.getState()).thenReturn(State.CLINGING);
        when(health.isDamageTaken()).thenReturn(false);
        when(input.down()).thenReturn(false);
        when(worldQuery.getTileFlags(anyInt(), anyInt())).thenReturn(Flags.CLINGABLE.bit());

        grapplingHooks.update(worldQuery, input);

        verify(player, never()).releaseCling();
    }

    @Test
    void shouldNotReleaseWhenNotClinging() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.getState()).thenReturn(State.WALKING);

        grapplingHooks.update(worldQuery, input);

        verify(player, never()).releaseCling();
    }

    @Test
    void shouldReleaseWhenDownIsPressed() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.getState()).thenReturn(State.CLINGING);
        when(input.down()).thenReturn(true);

        grapplingHooks.update(worldQuery, input);

        verify(player).releaseCling();
    }

    @Test
    void shouldReleaseWhenDamageTaken() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.getState()).thenReturn(State.CLINGING);
        when(input.down()).thenReturn(false);
        when(player.getHealth()).thenReturn(health);
        when(health.isDamageTaken()).thenReturn(true);

        grapplingHooks.update(worldQuery, input);

        verify(player).releaseCling();
    }

    @Test
    void shouldPullUpWhenUpIsPressedAndCanPullUp() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.getState()).thenReturn(State.CLINGING);
        when(input.up()).thenReturn(true);
        when(player.getHealth()).thenReturn(health);
        when(worldQuery.isSolid(anyInt(), anyInt())).thenReturn(false);

        grapplingHooks.update(worldQuery, input);

        verify(player).pullUp();
    }

    @Test
    void shouldNotPullUpWhenUpIsPressedAndCanNotPullUp() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.getState()).thenReturn(State.CLINGING);
        when(input.up()).thenReturn(true);
        when(player.getHealth()).thenReturn(health);
        when(worldQuery.isSolid(anyInt(), anyInt())).thenReturn(true);

        grapplingHooks.update(worldQuery, input);

        verify(player, never()).pullUp();
    }

    @Test
    void shouldNotPullUpWhenUpIsNotPressed() {
        when(worldQuery.getPlayer()).thenReturn(player);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)).thenReturn(true);
        when(player.getState()).thenReturn(State.CLINGING);
        when(input.up()).thenReturn(false);
        when(player.getHealth()).thenReturn(health);

        grapplingHooks.update(worldQuery, input);

        verify(player, never()).pullUp();
    }
}

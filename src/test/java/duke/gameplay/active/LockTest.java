package duke.gameplay.active;

import duke.dialog.Hints;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.items.Key;
import duke.gameplay.player.Inventory;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LockTest {
    private GameplayContext context;

    private Lock lock;

    @BeforeEach
    void createLock() {
        context = GameplayContextFixture.create();

        Inventory inventory = mock();
        when(context.getPlayer().getInventory()).thenReturn(inventory);

        lock = new Lock(0, 0, Key.Type.RED);
    }

    @Test
    void shouldStartLocked() {
        assertThat(lock.isLocked()).isTrue();
    }

    @Test
    void shouldBeInteractable() {
        when(context.getPlayer().intersects(lock)).thenReturn(true);
        when(context.getPlayer().getInventory().hasKey(any())).thenReturn(true);

        assertThat(lock.canInteract(context.getPlayer())).isTrue();
    }

    @Test
    void shouldNotBeInteractableWhenFarAway() {
        when(context.getPlayer().intersects(lock)).thenReturn(false);

        assertThat(lock.canInteract(context.getPlayer())).isFalse();
    }

    @Test
    void shouldNotBeInteractableWhenUnlocked() {
        when(context.getPlayer().getInventory().useKey(any())).thenReturn(true);

        lock.interactRequested(context);

        assertThat(lock.canInteract(context.getPlayer())).isFalse();
    }

    @Test
    void shouldUnlockWhenInteracting() {
        Door door = mock();
        when(door.requiresKey(any())).thenReturn(true);
        when(context.getActiveManager().getActives()).thenReturn(List.of(door));
        Lock lock = new Lock(0, 0, Key.Type.RED);
        when(context.getPlayer().getInventory().useKey(any())).thenReturn(true);

        lock.interactRequested(context);

        assertThat(lock.isLocked()).isFalse();
        verify(door).open(context);
    }

    @Test
    void shouldNotOpenOtherDoorWhenUnlocking() {
        Door door = mock();
        when(door.requiresKey(any())).thenReturn(false);
        when(context.getActiveManager().getActives()).thenReturn(List.of(door));
        Lock lock = new Lock(0, 0, Key.Type.RED);
        when(context.getPlayer().getInventory().useKey(any())).thenReturn(true);

        lock.interactRequested(context);

        assertThat(lock.isLocked()).isFalse();
        verify(door, never()).open(any());
    }

    @Test
    void shouldShowHint() {
        when(context.getPlayer().intersects(lock)).thenReturn(true);

        lock.update(context);

        verify(context.getHints()).showHint(Hints.Hint.LOCK, context);
    }

    @Test
    void shouldShowDialogWhenUnlockingWithoutProperKey() {
        when(context.getPlayer().intersects(lock)).thenReturn(true);
        when(context.getPlayer().getInventory().hasKey(any())).thenReturn(false);

        lock.interactRequested(context);

        assertThat(lock.isLocked()).isTrue();
        verify(context.getSoundManager()).play(Sfx.CHEAT_MODE);
        verify(context.getDialogManager()).open(any());
    }
}

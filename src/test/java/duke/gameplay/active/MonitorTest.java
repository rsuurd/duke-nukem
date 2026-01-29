package duke.gameplay.active;

import duke.dialog.Dialog;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.level.LevelDescriptor;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MonitorTest {
    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBeReady() {
        Monitor monitor = new Monitor(0, 0);

        assertThat(monitor.getState()).isEqualTo(Monitor.State.READY);
    }

    @Test
    void shouldBroadcastWhenReadyAndPlayerClose() {
        when(context.getLevel().getDescriptor()).thenReturn(new LevelDescriptor(1, 0, "Hello, world!"));

        Monitor monitor = new Monitor(0, 0);

        monitor.update(context);

        verify(context.getDialogManager()).open(Dialog.notes("Hello, world!"));
        verify(context.getSoundManager()).play(Sfx.MONITOR);
        assertThat(monitor.getState()).isEqualTo(Monitor.State.TRACKING);
    }

    @Test
    void shouldStayReadyWhenInvisible() {
        Monitor monitor = new Monitor(0, 0);

        monitor.setVisible(false);

        assertThat(monitor.getState()).isEqualTo(Monitor.State.READY);
    }

    @Test
    void shouldSwitchToStaticWhenTrackingAndInvisible() {
        Monitor monitor = new Monitor(0, 0, Monitor.State.TRACKING);

        monitor.setVisible(false);

        assertThat(monitor.getState()).isEqualTo(Monitor.State.STATIC);
    }

    @Test
    void shouldBeInteractable() {
        Monitor monitor = new Monitor(0, 0);
        when(context.getPlayer().intersects(monitor)).thenReturn(true);

        assertThat(monitor.canInteract(context.getPlayer())).isTrue();
    }

    @Test
    void shouldBroadcastWhenInteracting() {
        when(context.getLevel().getDescriptor()).thenReturn(new LevelDescriptor(1, 0, "Hello, world!"));
        Monitor monitor = new Monitor(0, 0);

        monitor.interactRequested(context);

        verify(context.getDialogManager()).open(Dialog.notes("Hello, world!"));
        verify(context.getSoundManager()).play(Sfx.MONITOR);
        assertThat(monitor.getState()).isEqualTo(Monitor.State.TRACKING);
    }
}
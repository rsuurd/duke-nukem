package duke.gameplay;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ViewportManagerTest {
    @Test
    void shouldTrackTarget() {
        Movable target = mock(Movable.class);

        ViewportManager manager = new ViewportManager(target, false);

        assertThat(manager.getTarget()).isEqualTo(target);
    }

    @Test
    void shouldChangeTarget() {
        Movable target = mock(Movable.class);
        Movable other = mock(Movable.class);

        ViewportManager manager = new ViewportManager(target, false);
        manager.setTarget(other);

        assertThat(manager.getTarget()).isEqualTo(other);
    }

    @Test
    void shouldRequestSnapToCenterAndReset() {
        ViewportManager manager = new ViewportManager(mock(), false);

        assertThat(manager.pollSnapToCenter()).isFalse();

        manager.snapToCenter();

        assertThat(manager.pollSnapToCenter()).isTrue();
        assertThat(manager.pollSnapToCenter()).isFalse();
    }
}
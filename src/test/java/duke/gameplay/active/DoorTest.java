package duke.gameplay.active;

import duke.gameplay.active.items.Key;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class DoorTest {
    @Test
    void shouldBeSolid() {
        Door door = new Door(0, 0, Key.Type.BLUE);

        assertThat(door.isSolid()).isTrue();
    }

    @Test
    void shouldOpen() {
        Door door = new Door(0, 0, Key.Type.BLUE);

        door.open();

        for (int i = 0; i < 8; i++) {
            door.update(mock());
            assertThat(door.isDestroyed()).isFalse();
        }

        door.update(mock());
        assertThat(door.isDestroyed()).isTrue();
    }
}

package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.items.Key;
import duke.sfx.Sfx;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DoorTest {
    @Test
    void shouldBeSolid() {
        Door door = new Door(0, 0, Key.Type.BLUE);

        assertThat(door.isSolid()).isTrue();
    }

    @Test
    void shouldOpen() {
        GameplayContext context = GameplayContextFixture.create();
        Door door = new Door(0, 0, Key.Type.BLUE);

        door.open(context);

        verify(context.getSoundManager()).play(Sfx.OPEN_KEY_DOOR);

        for (int i = 0; i < 8; i++) {
            assertThat(door.isDestroyed()).isFalse();
            door.update(mock());
        }

        assertThat(door.isDestroyed()).isTrue();
    }
}

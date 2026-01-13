package duke.gameplay.active.items;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KeyTest {
    @Test
    void shouldHangSuspended() {
        Key key = new Key(0, 0, Key.Type.RED);

        assertThat(key.getVerticalAcceleration()).isEqualTo(0);
    }
}

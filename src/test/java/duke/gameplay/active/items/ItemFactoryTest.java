package duke.gameplay.active.items;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemFactoryTest {
    @Test
    void shouldCreateFootball() {
        assertThat(ItemFactory.createFootball(16, 32)).isNotNull();
    }

    @Test
    void shouldCreateFloppy() {
        assertThat(ItemFactory.createFloppy(16, 32)).isNotNull();
    }

    @Test
    void shouldCreateJoystick() {
        assertThat(ItemFactory.createJoystick(16, 32)).isNotNull();
    }

    @Test
    void shouldCreateFlag() {
        assertThat(ItemFactory.createFlag(16, 32)).isNotNull();
    }

    @Test
    void shouldCreateRadio() {
        assertThat(ItemFactory.createRadio(16, 32)).isNotNull();
    }
}

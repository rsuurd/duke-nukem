package duke.gameplay;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActiveTest {


    @Test
    void shouldIndicateActive() {
        assertThat(create().isActive()).isTrue();
    }

    @Test
    void shouldDeactivate() {
        Active active = create();

        active.deactivate();

        assertThat(active.isActive()).isFalse();
    }

    private Active create() {
        return new Active(0, 0, 8, 8) {
        };
    }
}

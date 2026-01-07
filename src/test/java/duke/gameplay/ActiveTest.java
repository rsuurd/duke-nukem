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

    @Test
    void shouldCalculateRowAndCol() {
        Active active = create(128, 32);

        assertThat(active.getRow()).isEqualTo(2);
        assertThat(active.getCol()).isEqualTo(8);
    }

    private Active create() {
        return create(0, 0);
    }

    private Active create(int x, int y) {
        return new Active(x, y, 8, 8) {
        };
    }
}

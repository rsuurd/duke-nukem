package duke.gameplay;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActiveTest {
    @Test
    void shouldIndicateInactive() {
        assertThat(create().isActivated()).isFalse();
    }

    @Test
    void shouldActivate() {
        Active active = create();

        active.activate();

        assertThat(active.isActivated()).isTrue();
    }

    @Test
    void shouldIndicateIntact() {
        assertThat(create().isDestroyed()).isFalse();
    }

    @Test
    void shouldDestroy() {
        Active active = create();

        active.destroy();

        assertThat(active.isDestroyed()).isTrue();
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

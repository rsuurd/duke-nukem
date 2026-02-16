package duke.gameplay;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActiveTest {
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

    @Test
    void shouldAddAndResetExternalVelocity() {
        Active active = create();

        active.addExternalVelocityX(5);
        active.addExternalVelocityY(-3);

        assertThat(active.getExternalVelocityX()).isEqualTo(5);
        assertThat(active.getExternalVelocityY()).isEqualTo(-3);

        active.resetExternalVelocity();

        assertThat(active.getExternalVelocityX()).isEqualTo(0);
        assertThat(active.getExternalVelocityY()).isEqualTo(0);
    }

    private Active create() {
        return create(0, 0);
    }

    private Active create(int x, int y) {
        return new Active(x, y, 8, 8) {
        };
    }
}

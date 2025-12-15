package duke.gfx;

import org.junit.jupiter.api.Test;

import static duke.gfx.Viewport.*;
import static org.assertj.core.api.Assertions.assertThat;

class ViewportTest {
    @Test
    void shouldCenter() {
        Viewport viewport = new Viewport();

        viewport.center(0, 0);

        assertThat(viewport.getX()).isEqualTo(-HORIZONTAL_CENTER);
        assertThat(viewport.getY()).isEqualTo(-VERTICAL_CENTER);
    }

    @Test
    void shouldMoveLeft() {
        Viewport viewport = new Viewport();

        viewport.update(16, 0, false);

        assertThat(viewport.getX()).isEqualTo(16 - LEFT_BOUND);
    }

    @Test
    void shouldMoveRight() {
        Viewport viewport = new Viewport();

        viewport.update(RIGHT_BOUND + 16, 0, false);

        assertThat(viewport.getX()).isEqualTo(16);
    }

    @Test
    void shouldNotMoveHorizontally() {
        Viewport viewport = new Viewport();

        for (int x = LEFT_BOUND; x <= RIGHT_BOUND; x++) {
            viewport.update(x, 0, false);
            assertThat(viewport.getX()).isEqualTo(0);
        }
    }

    @Test
    void shouldMoveUp() {
        Viewport viewport = new Viewport();

        viewport.update(0, 16, false);

        assertThat(viewport.getY()).isEqualTo(16 - UPPER_BOUND);
    }

    @Test
    void shouldMoveDown() {
        Viewport viewport = new Viewport();

        viewport.update(0, LOWER_BOUND + 16, false);

        assertThat(viewport.getY()).isEqualTo(16);
    }

    @Test
    void shouldNotMoveVertically() {
        Viewport viewport = new Viewport();

        for (int y = UPPER_BOUND; y <= LOWER_BOUND; y++) {
            viewport.update(0, y, false);
            assertThat(viewport.getY()).isEqualTo(0);
        }
    }

    @Test
    void shouldCenterVertically() {
        Viewport viewport = new Viewport();

        int targetY = VERTICAL_CENTER + 32;

        viewport.update(0, targetY, true);
        assertThat(viewport.getY()).isEqualTo(16);
        viewport.update(0, targetY, true);
        assertThat(viewport.getY()).isEqualTo(32);
    }

    @Test
    void shouldDetermineScreenCoordinates() {
        Viewport viewport = new Viewport();

        assertThat(viewport.toScreenX(0)).isEqualTo(0);
        assertThat(viewport.toScreenY(0)).isEqualTo(0);

        viewport.update(100, 50, true);
        assertThat(viewport.toScreenX(100)).isEqualTo(100 - viewport.getX());
        assertThat(viewport.toScreenY(50)).isEqualTo(50 - viewport.getY());
    }
}

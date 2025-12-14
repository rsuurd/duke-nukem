package duke.gfx;

import org.junit.jupiter.api.Test;

import static duke.gfx.Viewport.*;
import static org.assertj.core.api.Assertions.assertThat;

class ViewportTest {
    @Test
    void shouldMoveLeft() {
        Viewport viewport = create();

        viewport.update(16, 0, false);

        assertThat(viewport.getX()).isEqualTo(16 - LEFT_BOUND);
    }

    @Test
    void shouldMoveRight() {
        Viewport viewport = create();

        viewport.update(RIGHT_BOUND + 16, 0, false);

        assertThat(viewport.getX()).isEqualTo(16);
    }

    @Test
    void shouldNotMoveHorizontally() {
        Viewport viewport = create();

        for (int x = LEFT_BOUND; x <= RIGHT_BOUND; x++) {
            viewport.update(x, 0, false);
            assertThat(viewport.getX()).isEqualTo(0);
        }
    }

    @Test
    void shouldMoveUp() {
        Viewport viewport = create();

        viewport.update(0, 16, false);

        assertThat(viewport.getY()).isEqualTo(16 - UPPER_BOUND);
    }

    @Test
    void shouldMoveDown() {
        Viewport viewport = create();

        viewport.update(0, LOWER_BOUND + 16, false);

        assertThat(viewport.getY()).isEqualTo(16);
    }

    @Test
    void shouldNotMoveVertically() {
        Viewport viewport = create();

        for (int y = UPPER_BOUND; y <= LOWER_BOUND; y++) {
            viewport.update(0, y, false);
            assertThat(viewport.getY()).isEqualTo(0);
        }
    }

    @Test
    void shouldCenterVertically() {
        Viewport viewport = create();

        int targetY = VERTICAL_CENTER + 32;

        viewport.update(0, targetY, true);
        assertThat(viewport.getY()).isEqualTo(16);
        viewport.update(0, targetY, true);
        assertThat(viewport.getY()).isEqualTo(32);
    }

    private Viewport create() {
        return new Viewport();
    }
}

package duke.gfx;

import duke.gameplay.Active;
import duke.gameplay.Movable;
import duke.gameplay.player.Player;
import duke.gameplay.player.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static duke.gfx.Viewport.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ViewportTest {
    @Test
    void shouldCenter() {
        Viewport viewport = new Viewport();

        viewport.center(createTarget(0, 0));

        assertThat(viewport.getX()).isEqualTo(-HORIZONTAL_CENTER);
        assertThat(viewport.getY()).isEqualTo(-VERTICAL_CENTER);
    }

    @Test
    void shouldMoveLeft() {
        Viewport viewport = new Viewport(192, 0);

        viewport.update(createTarget(116, 0));

        assertThat(viewport.getX()).isEqualTo(116 - LEFT_BOUND);
    }

    @Test
    void shouldNotGoOutOfLevelBounds() {
        Viewport viewport = new Viewport();
        viewport.update(createTarget(0, 0));

        assertThat(viewport.getX()).isEqualTo(0);

        viewport.update(createTarget(2048, 0));

        assertThat(viewport.getX()).isEqualTo(RIGHT_CAP);
    }

    @Test
    void shouldMoveRight() {
        Viewport viewport = new Viewport();

        viewport.update(createTarget(RIGHT_BOUND + 16, 0));

        assertThat(viewport.getX()).isEqualTo(16);
    }

    @Test
    void shouldNotMoveHorizontally() {
        Viewport viewport = new Viewport();

        for (int x = LEFT_BOUND; x <= RIGHT_BOUND; x++) {
            viewport.update(createTarget(x, 0));
            assertThat(viewport.getX()).isEqualTo(0);
        }
    }

    @Test
    void shouldMoveUp() {
        Viewport viewport = new Viewport();

        viewport.update(createTarget(0, 16));

        assertThat(viewport.getY()).isEqualTo(16 - UPPER_BOUND);
    }

    @Test
    void shouldMoveDown() {
        Viewport viewport = new Viewport();

        viewport.update(createTarget(0, LOWER_BOUND + 16));

        assertThat(viewport.getY()).isEqualTo(16);
    }

    @Test
    void shouldNotMoveVertically() {
        Viewport viewport = new Viewport();

        for (int y = UPPER_BOUND; y <= LOWER_BOUND; y++) {
            viewport.update(createTarget(0, y));
            assertThat(viewport.getY()).isEqualTo(0);
        }
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"STANDING", "WALKING", "CLINGING"})
    void shouldSmoothCenterVertically(State state) {
        Viewport viewport = new Viewport();
        int targetY = VERTICAL_CENTER + 32;

        Player player = mock();
        when(player.getState()).thenReturn(state);
        when(player.getX()).thenReturn(0);
        when(player.getY()).thenReturn(targetY);

        viewport.update(player);
        assertThat(viewport.getY()).isEqualTo(16);

        viewport.update(player);
        assertThat(viewport.getY()).isEqualTo(32);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"STANDING", "WALKING", "CLINGING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldNotSmoothCenterVertically(State state) {
        Viewport viewport = new Viewport();
        int targetY = VERTICAL_CENTER + 128;

        Player player = mock();
        when(player.getState()).thenReturn(state);
        when(player.getX()).thenReturn(0);
        when(player.getY()).thenReturn(targetY);

        viewport.update(player);
        assertThat(viewport.getY()).isEqualTo(96);

        viewport.update(player);
        assertThat(viewport.getY()).isEqualTo(96);
    }

    @Test
    void shouldDetermineScreenCoordinates() {
        Viewport viewport = new Viewport();

        assertThat(viewport.toScreenX(0)).isEqualTo(16);
        assertThat(viewport.toScreenY(0)).isEqualTo(16);

        viewport.update(createTarget(100, 50));
        assertThat(viewport.toScreenX(100)).isEqualTo(116 - viewport.getX());
        assertThat(viewport.toScreenY(50)).isEqualTo(66 - viewport.getY());
    }

    @Test
    void shouldDetermineVisibility() {
        Viewport viewport = new Viewport();
        Movable movable = new Active(0, 0, 16, 16) {
        };

        assertThat(viewport.isVisible(movable)).isTrue();

        movable.setX(-8);
        movable.setY(-8);
        assertThat(viewport.isVisible(movable)).isTrue();

        movable.setX(-movable.getWidth());
        movable.setY(-movable.getHeight());

        assertThat(viewport.isVisible(movable)).isFalse();
    }

    private Movable createTarget(int x, int y) {
        return new Active(x, y, 0, 0) {
        };
    }
}

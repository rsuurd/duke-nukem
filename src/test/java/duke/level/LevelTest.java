package duke.level;

import duke.gameplay.Active;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LevelTest {
    @Test
    void shouldRejectIncorrectSize() {
        assertThatThrownBy(() ->
                new Level(0, new int[0], 0, 0, Collections.emptyList())
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("Unexpected level size");
    }

    @Test
    void shouldResolveTiles() {
        Level level = new Level(0, new int[Level.WIDTH * Level.HEIGHT], 0, 0, Collections.emptyList());

        assertThat(level.getTile(0, 0)).isEqualTo(0);
    }

    @Test
    void shouldResolveDefaultTile() {
        Level level = new Level(0, new int[Level.WIDTH * Level.HEIGHT], 0, 0, Collections.emptyList());

        assertThat(level.getTile(-1, 0)).isEqualTo(0);
        assertThat(level.getTile(0, -1)).isEqualTo(0);
        assertThat(level.getTile(Level.HEIGHT, 0)).isEqualTo(0);
        assertThat(level.getTile(0, Level.WIDTH)).isEqualTo(0);
    }

    @Test
    void shouldGetPlayerStartLocation() {
        Level level = new Level(0, new int[Level.WIDTH * 90], 0, 562, Collections.emptyList());

        assertThat(level.getPlayerStartX()).isEqualTo(800);
        assertThat(level.getPlayerStartY()).isEqualTo(64);
    }

    @Test
    void shouldIndicateTileIsSolid() {
        int[] tiles = new int[Level.WIDTH * 90];
        tiles[0] = Level.BACKGROUNDS;
        tiles[1] = Level.SOLIDS;

        Level level = new Level(0, tiles, 0, 562, Collections.emptyList());

        assertThat(level.isSolid(0, 0)).isFalse();
        assertThat(level.isSolid(0, 1)).isTrue();
    }

    @Test
    void shouldNotAllowAddingActivesToLevel() {
        Level level = new Level(0, new int[Level.WIDTH * 90], 0, 0, new ArrayList<>());

        assertThatThrownBy(() ->
                level.getActives().add(new Active(0, 0, 0, 0) {})
        ).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldConvertAddressToCoordinates() {
        assertThat(Level.toX(562)).isEqualTo(800);
        assertThat(Level.toY(562)).isEqualTo(64);
    }

    @Test
    void shouldSetTile() {
        Level level = new Level(0, new int[Level.WIDTH * Level.HEIGHT], 0, 0, Collections.emptyList());

        level.setTile(3, 3, 1);

        assertThat(level.getTile(3, 3)).isEqualTo(1);
    }

    @Test
    void shouldIgnoreInvalidLocationWhenSettingTile() {
        Level level = new Level(0, new int[Level.WIDTH * Level.HEIGHT], 0, 0, Collections.emptyList());

        level.setTile(-1, -1, 1);
        level.setTile(Level.HEIGHT, Level.WIDTH, 1);
    }
}

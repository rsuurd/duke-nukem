package duke.level;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LevelTest {
    @Test
    void shouldRejectIncorrectSize() {
        assertThatThrownBy(() ->
                new Level(0, new int[0], 0, 0)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("Unexpected level size");
    }

    @Test
    void shouldResolveTiles() {
        Level level = new Level(0, new int[Level.WIDTH * 90], 0, 0);

        assertThat(level.getTile(0, 0)).isEqualTo(0);
    }

    @Test
    void shouldResolveDefaultTile() {
        Level level = new Level(0, new int[Level.WIDTH * 90], 0, 0);

        assertThat(level.getTile(-1, 0)).isEqualTo(0);
        assertThat(level.getTile(0, -1)).isEqualTo(0);
        assertThat(level.getTile(Level.HEIGHT, 0)).isEqualTo(0);
        assertThat(level.getTile(0, Level.WIDTH)).isEqualTo(0);
    }

    @Test
    void shouldGetPlayerStartLocation() {
        Level level = new Level(0, new int[Level.WIDTH * 90], 0, 562);

        assertThat(level.getPlayerStartX()).isEqualTo(800);
        assertThat(level.getPlayerStartY()).isEqualTo(64);
    }

    @Test
    void shouldIndicateTileIsSolid() {
        int[] tiles = new int[Level.WIDTH * 90];
        tiles[0] = Level.BACKGROUNDS;
        tiles[1] = Level.SOLIDS;

        Level level = new Level(0, tiles, 0, 562);

        assertThat(level.isSolid(0, 0)).isFalse();
        assertThat(level.isSolid(0, 1)).isTrue();
    }
}

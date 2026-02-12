package duke.level;

import duke.gameplay.Active;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class LevelTest {
    @Test
    void shouldRejectIncorrectSize() {
        assertThatThrownBy(() ->
                new Level(mock(), new int[0], 0, Collections.emptyList())
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("Unexpected level size");
    }

    @Test
    void shouldResolveTiles() {
        Level level = new Level(mock(), new int[Level.WIDTH * Level.HEIGHT], 0, Collections.emptyList());

        assertThat(level.getTile(0, 0)).isEqualTo(0);
    }

    @Test
    void shouldResolveDefaultTile() {
        Level level = new Level(mock(), new int[Level.WIDTH * Level.HEIGHT], 0, Collections.emptyList());

        assertThat(level.getTile(-1, 0)).isEqualTo(0);
        assertThat(level.getTile(0, -1)).isEqualTo(0);
        assertThat(level.getTile(Level.HEIGHT, 0)).isEqualTo(0);
        assertThat(level.getTile(0, Level.WIDTH)).isEqualTo(0);
    }

    @Test
    void shouldGetPlayerStartLocation() {
        Level level = new Level(mock(), new int[Level.WIDTH * 90], 562, Collections.emptyList());

        assertThat(level.getPlayerStartX()).isEqualTo(800);
        assertThat(level.getPlayerStartY()).isEqualTo(48);
    }

    @Test
    void shouldNotAllowAddingActivesToLevel() {
        Level level = new Level(mock(), new int[Level.WIDTH * 90], 0, new ArrayList<>());

        assertThatThrownBy(() ->
                level.getActives().add(new Active(0, 0, 0, 0) {
                })
        ).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldConvertAddressToCoordinates() {
        assertThat(Level.toX(562)).isEqualTo(800);
        assertThat(Level.toY(562)).isEqualTo(64);
    }

    @Test
    void shouldSetTile() {
        Level level = new Level(mock(), new int[Level.WIDTH * Level.HEIGHT], 0, Collections.emptyList());

        level.setTile(3, 3, 1);

        assertThat(level.getTile(3, 3)).isEqualTo(1);
    }

    @Test
    void shouldIgnoreInvalidLocationWhenSettingTile() {
        Level level = new Level(mock(), new int[Level.WIDTH * Level.HEIGHT], 0, Collections.emptyList());

        level.setTile(-1, -1, 1);
        level.setTile(Level.HEIGHT, Level.WIDTH, 1);
    }

    @Test
    void shouldCompleteLevel() {
        Level level = new Level(mock(), new int[Level.WIDTH * Level.HEIGHT], 0, Collections.emptyList());

        assertThat(level.isCompleted()).isFalse();

        level.complete();

        assertThat(level.isCompleted()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("gratedTileIds")
    void shouldGetTileFlags(int tileId) {
        int[] tiles = new int[Level.WIDTH * 90];

        tiles[1] = Level.SOLIDS;
        tiles[2] = tileId;

        Level level = new Level(mock(), tiles, 0, Collections.emptyList());

        assertThat(Flags.SOLID.isSet(level.getTileFlags(0, 1))).isTrue();
        assertThat(Flags.SOLID.isSet(level.getTileFlags(0, 2))).isTrue();
        assertThat(Flags.CLINGABLE.isSet(level.getTileFlags(0, 2))).isTrue();
    }

    static Stream<Arguments> gratedTileIds() {
        return Stream.concat(
                IntStream.iterate(Level.CONVEYOR, tileId -> tileId <= Level.CONVEYOR_END, tileId -> tileId + 32).boxed(),
                IntStream.iterate(Level.CLINGABLE, tileId -> tileId < Level.ACTIVE, tileId -> tileId + 32).boxed()
        ).map(Arguments::of);
    }
}

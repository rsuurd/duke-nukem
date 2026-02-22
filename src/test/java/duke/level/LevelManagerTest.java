package duke.level;

import duke.DukeNukemException;
import duke.resources.AssetManager;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LevelManagerTest {
    @Test
    void shouldGetNextLevel() {
        AssetManager assetManager = mock();
        LevelDescriptor level1 = mock();
        LevelManager manager = new LevelManager(assetManager, List.of(level1));

        manager.getNextLevel();

        verify(assetManager).getLevel(level1);
    }

    @Test
    void shouldFailWhenOutOfLevels() {
        AssetManager assetManager = mock();
        LevelManager manager = new LevelManager(assetManager, List.of());

        assertThatThrownBy(manager::getNextLevel).isInstanceOf(DukeNukemException.class).hasMessage("No more levels to beat");
    }

    @Test
    void shouldWarpToLevel() {
        AssetManager assetManager = mock();

        List<LevelDescriptor> levels = IntStream.range(0, 3).mapToObj(id -> new LevelDescriptor(id, id, 0)).toList();

        LevelManager manager = new LevelManager(assetManager, levels);

        manager.warpTo(1);

        verify(assetManager).getLevel(levels.get(1));
    }

    @Test
    void shouldIndicateLastLevel() {
        LevelManager manager = new LevelManager(mock(), List.of(new LevelDescriptor(0, 1, 1)));

        manager.getNextLevel();

        assertThat(manager.isLast()).isTrue();
    }
}

package duke.level.processors;

import duke.gameplay.active.enemies.WallCrawler;
import duke.level.LevelBuilder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static duke.level.LevelBuilder.LEFT;
import static duke.level.LevelBuilder.RIGHT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WallCrawlerProcessorTest {
    @Mock
    private LevelBuilder builder;

    private ActiveProcessor processor = new WallCrawlerProcessor();

    @ParameterizedTest
    @MethodSource("tileIds")
    void shouldProcess(int tileId, int offset) {
        when(builder.add(any())).thenReturn(builder);

        assertThat(processor.canProcess(tileId)).isTrue();

        processor.process(20, tileId, builder);

        verify(builder).add(isA(WallCrawler.class));
        verify(builder).replaceTile(20, offset);
    }

    static Stream<Arguments> tileIds() {
        return Stream.of(
                Arguments.of(WallCrawlerProcessor.TILE_ID_RIGHT, RIGHT),
                Arguments.of(WallCrawlerProcessor.TILE_ID_LEFT, LEFT)
        );
    }
}
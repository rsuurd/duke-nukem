package duke.gameplay;

import duke.gameplay.effects.Score;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ScoreManagerTest {
    @Mock
    private ActiveManager activeManager;

    @InjectMocks
    private ScoreManager manager;

    @Test
    void shouldIncreaseScore() {
        manager.score(1);

        assertThat(manager.getScore()).isEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 200, 500, 1000, 2000, 5000, 10000})
    void shouldIncreaseScoreWithIndicator(int points) {
        manager.score(points, 0, 200);

        assertThat(manager.getScore()).isEqualTo(points);

        verify(activeManager).spawn(any(Score.class));
    }

    @Test
    void shouldNotSpawnUnsupportedScoreEffect() {
        manager.score(300, 0, 200);

        assertThat(manager.getScore()).isEqualTo(300);

        verifyNoInteractions(activeManager);
    }
}

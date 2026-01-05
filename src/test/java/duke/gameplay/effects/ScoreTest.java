package duke.gameplay.effects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ScoreTest {
    @ParameterizedTest
    @ValueSource(ints = {100, 200, 500, 1000, 2000, 5000, 10000})
    void shouldSupportValidScores(int points) {
        assertThat(Score.supports(points)).isTrue();
    }

    @Test
    void shouldFloatUp() {
        Score score = new Score(0, 100, 1000);

        score.update(mock());

        assertThat(score.getY()).isEqualTo(98);
    }
}

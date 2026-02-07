package duke;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class GameParametersTest {
    @ParameterizedTest
    @ValueSource(strings = {"asp", " ASP"})
    void shouldParseAspParameter(String parameter) {
        GameParameters params = GameParameters.parse(parameter);

        assertThat(params.asp()).isTrue();
    }

    @Test
    void shouldParseDefault() {
        GameParameters params = GameParameters.parse();

        assertThat(params).isEqualTo(new GameParameters(false));
    }
}

package duke;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class GameParametersTest {
    @ParameterizedTest
    @ValueSource(strings = {"asp", " ASP"})
    void shouldParseAspParameter(String parameter) {
        GameParameters params = GameParameters.parse(parameter);

        assertThat(params.asp()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"--path=foo", " -p=foo", "--Path=foo", " -P=foo"})
    void shouldParsePathParameter(String parameter) {
        GameParameters params = GameParameters.parse(parameter);

        assertThat(params.path()).isEqualTo(Paths.get("foo"));
    }

    @Test
    void shouldParseDefault() {
        GameParameters params = GameParameters.parse();

        assertThat(params).isEqualTo(new GameParameters(Paths.get(".dn1"), false));
    }
}

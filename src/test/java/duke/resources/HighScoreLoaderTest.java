package duke.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HighScoreLoaderTest {
    @Mock
    private Path path;

    @InjectMocks
    private HighScoreLoader loader;

    @Test
    void shouldLoadHighScores() throws IOException {
        when(path.resolve(anyString())).thenReturn(createTemp("40000TODD"));

        List<HighScoreLoader.HighScore> highScores = loader.load();

        assertThat(highScores).containsExactly(new HighScoreLoader.HighScore("TODD", 40000));
    }

    @Test
    void shouldNotExceedMaxInt() throws IOException {
        when(path.resolve(anyString())).thenReturn(createTemp("9999999999999DUKE"));

        List<HighScoreLoader.HighScore> highScores = loader.load();

        assertThat(highScores).containsExactly(new HighScoreLoader.HighScore("DUKE", Integer.MAX_VALUE));
    }

    private Path createTemp(String... highScoreData) throws IOException {
        Path temp = Files.createTempFile("highs", "dn1");

        for (String line : highScoreData) {
            Files.writeString(temp, line + System.lineSeparator(), java.nio.file.StandardOpenOption.APPEND);
        }

        temp.toFile().deleteOnExit();

        return temp;
    }
}

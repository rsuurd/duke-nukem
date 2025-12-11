package duke.resources;

import duke.level.LevelData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LevelLoaderTest {
    @Mock
    private Path path;

    @InjectMocks
    private LevelLoader loader;

    @Test
    void shouldLoadLevelData() throws IOException {
        when(path.resolve(anyString())).thenReturn(createTempLevelData());

        LevelData data = loader.readLevel(1);

        assertThat(data).isNotNull();
        for (int row = 0; row < 90; row++) {
            for (int col = 0; col < 128; col++) {
                assertThat(data.getTile(row, col)).isEqualTo(1);
            }
        }
    }

    private Path createTempLevelData() throws IOException {
        Path temp = Files.createTempFile("worldal", "dn1");

        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(temp))) {
            short[] data = new short[LEVEL_SIZE];
            Arrays.fill(data, Short.reverseBytes((short) 1));

            for (short datum : data) {
                out.writeShort(datum);
            }
        }

        temp.toFile().deleteOnExit();

        return temp;
    }

    private static final int LEVEL_SIZE = 11520;
}

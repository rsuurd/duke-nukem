package duke.resources;

import duke.gfx.Sprite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpriteLoaderTest {
    @Mock
    private Path path;

    @InjectMocks
    private SpriteLoader spriteLoader;

    @Test
    void shouldLoadFullScreenImage() throws IOException {
        when(path.resolve(anyString())).thenReturn(createTempImage((byte) 0xFF));

        Sprite image = spriteLoader.readImage("FULLSCRN.DN1");

        assertThat(image).isNotNull();
        assertThat(image.getWidth()).isEqualTo(320);
        assertThat(image.getHeight()).isEqualTo(200);
        assertThat(image.getPixel(0, 0)).isEqualTo((byte) 15);
    }

    @Test
    void shouldLoadBackdrop() throws IOException {
        when(path.resolve(anyString())).thenReturn(createTempTiles(10 * 13, (byte) 0xFF));

        Sprite image = spriteLoader.readBackdrop(0);

        assertThat(image).isNotNull();
        assertThat(image.getWidth()).isEqualTo(208);
        assertThat(image.getHeight()).isEqualTo(160);
        assertThat(image.getPixel(0, 0)).isEqualTo((byte) 15);
    }

    @Test
    void shouldLoadTiles() throws IOException {
        when(path.resolve(anyString())).thenReturn(createTempTiles(1, (byte) 0xFF));

        List<Sprite> tiles = spriteLoader.readTiles("TILES.DN1", false);

        assertThat(tiles).hasSize(1);
        Sprite tile = tiles.getFirst();

        assertThat(tile).isNotNull();
        assertThat(tile.getWidth()).isEqualTo(16);
        assertThat(tile.getHeight()).isEqualTo(16);
        assertThat(tile.getPixel(0, 0)).isEqualTo( (byte) 15);
    }

    private Path createTempImage(byte value) throws IOException {
        Path temp = Files.createTempFile("image", ".dn1");

        try (OutputStream out = Files.newOutputStream(temp)) {
            for (int i = 0; i < 4; i++) {
                out.write(createPlane(8000, value));
            }
        }

        temp.toFile().deleteOnExit();

        return temp;
    }

    private Path createTempTiles(int count, byte value) throws IOException {
        Path temp = Files.createTempFile("image", ".dn1");

        try (OutputStream out = Files.newOutputStream(temp)) {
            out.write(new byte[]{(byte) count, 2, 16});
            out.write(createPlane(count * 160, value));
        }

        temp.toFile().deleteOnExit();

        return temp;
    }

    private byte[] createPlane(int size, byte value) {
        byte[] plane = new byte[size];
        Arrays.fill(plane, value);

        return plane;
    }
}

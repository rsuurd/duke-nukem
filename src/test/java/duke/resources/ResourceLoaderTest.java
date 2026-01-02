package duke.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ResourceLoaderTest {
    @Mock
    private SharewareDownloader downloader;

    @Mock
    private SpriteLoader spriteLoader;

    @Mock
    private LevelLoader levelLoader;

    @Mock
    private SoundLoader soundLoader;

    private ResourceLoader createResourceLoader() {
        try {
            return createResourceLoader(Files.createTempDirectory("dn"));
        } catch (IOException e) {
            return fail(e);
        }
    }

    private ResourceLoader createResourceLoader(Path path) {
        return new ResourceLoader(path, downloader, spriteLoader, levelLoader, soundLoader);
    }

    @Test
    void shouldDownloadIfDirectoryIsEmpty() {
        createResourceLoader().ensureResourcesExist();

        verify(downloader).download();
    }

    @Test
    void shouldNotDownloadIfFilesArePresent() throws IOException {
        Path path = Files.createTempDirectory("dn");
        Files.createFile(path.resolve("DN.DN1"));

        createResourceLoader(path).ensureResourcesExist();

        verifyNoInteractions(downloader);
    }

    @Test
    void shouldReturnSpriteLoader() {
        assertThat(createResourceLoader().getSpriteLoader()).isSameAs(spriteLoader);
    }

    @Test
    void shouldReturnLevelLoader() {
        assertThat(createResourceLoader().getLevelLoader()).isSameAs(levelLoader);
    }

    @Test
    void shouldReturnSoundLoader() {
        assertThat(createResourceLoader().getSoundLoader()).isSameAs(soundLoader);
    }
}

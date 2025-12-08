package duke.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ResourceLoaderTest {
    @Mock
    private SharewareDownloader downloader;
    
    private ResourceLoader createResourceLoader(Path path) {
        return new ResourceLoader(path, downloader);
    }

    @Test
    public void shouldDownloadIfDirectoryIsEmpty() throws IOException {
        createResourceLoader(Files.createTempDirectory("dn")).ensureResourcesExist();

        verify(downloader).download();
    }

    @Test
    public void shouldNotDownloadIfFilesArePresent() throws IOException {
        Path path = Files.createTempDirectory("dn");
        Files.createFile(path.resolve("DN.DN1"));

        createResourceLoader(path).ensureResourcesExist();

        verifyNoInteractions(downloader);
    }
}

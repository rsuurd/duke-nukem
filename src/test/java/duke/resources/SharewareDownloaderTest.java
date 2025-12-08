package duke.resources;

import duke.DukeNukemException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SharewareDownloaderTest {
    @Test
    void shouldDownload() throws IOException {
        Path path = Files.createTempDirectory("dn");
        SharewareDownloader downloader = new SharewareDownloader(path);

        downloader.download();

        assertThat(path).isDirectoryContaining("glob:**.DN1");
    }

    @Test
    void shouldIndicateFailure() throws IOException {
        Supplier<InputStream> streamSupplier = mock(Supplier.class);
        when(streamSupplier.get()).thenThrow(DukeNukemException.class);

        SharewareDownloader downloader = new SharewareDownloader(Files.createTempDirectory("dn"), streamSupplier);

        assertThatThrownBy(downloader::download)
                .isInstanceOf(DukeNukemException.class)
                .hasMessage("Could not download shareware episode");
    }
}

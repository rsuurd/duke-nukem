package duke.resources;

import duke.DukeNukemException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ResourceLoader {
    private Path path;
    private SharewareDownloader downloader;

    public ResourceLoader(Path path) {
        this(path, new SharewareDownloader(path));
    }

    protected ResourceLoader(Path path, SharewareDownloader downloader) {
        this.path = path;
        this.downloader = downloader;
    }

    public void ensureResourcesExist() {
        if (isEmptyDirectory(path)) {
            downloader.download();
        }
    }

    private boolean isEmptyDirectory(Path path) {
        try {
            Files.createDirectories(path);

            try (Stream<Path> stream = Files.list(path)) {
                return stream.findAny().isEmpty();
            }
        } catch (IOException e) {
            throw new DukeNukemException(String.format("Could not check base directory: %s", path), e);
        }
    }
}

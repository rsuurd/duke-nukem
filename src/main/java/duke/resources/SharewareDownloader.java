package duke.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class SharewareDownloader {
    private Path path;
    private Supplier<InputStream> streamSupplier;

    public SharewareDownloader(Path path) {
        this(path, () -> {
            try {
                return URI.create("https://image.dosgamesarchive.com/games/1duke.zip").toURL().openStream();
            } catch (IOException e) {
                throw new DukeNukemException("Could not download shareware episode", e);
            }
        });
    }

    protected SharewareDownloader(Path path, Supplier<InputStream> streamSupplier) {
        this.path = path;
        this.streamSupplier = streamSupplier;
    }

    public void download() {
        try (ZipInputStream zip = new ZipInputStream(streamSupplier.get())) {
            ensureFolderExists();

            ZipEntry entry;

            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().equalsIgnoreCase("DUKE.1")) {
                    zip.skip(0x3bf5);

                    extract(zip, path);
                }

                zip.closeEntry();
            }
        } catch (Exception e) {
            throw new DukeNukemException("Could not download shareware episode", e);
        }
    }

    private void ensureFolderExists() throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    private void extract(ZipInputStream zip, Path path) throws IOException {
        ZipInputStream nested = new ZipInputStream(zip);
        ZipEntry entry;

        while ((entry = nested.getNextEntry()) != null) {
            extractEntry(nested, entry, path);

            nested.closeEntry();
        }
    }

    private void extractEntry(ZipInputStream zip, ZipEntry entry, Path path) throws IOException {
        Path file = path.resolve(entry.getName());

        if (entry.isDirectory()) {
            Files.createDirectories(file);
        } else {
            Files.copy(zip, file, REPLACE_EXISTING);
        }
    }
}

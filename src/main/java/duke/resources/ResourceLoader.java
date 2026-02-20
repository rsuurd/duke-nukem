package duke.resources;

import duke.DukeNukemException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ResourceLoader {
    private Path path;

    private SharewareDownloader downloader;
    private SpriteLoader spriteLoader;
    private LevelLoader levelLoader;
    private SoundLoader soundLoader;
    private HighScoreLoader highScoreLoader;

    public ResourceLoader(Path path) {
        this(path, new SharewareDownloader(path), new SpriteLoader(path), new LevelLoader(path), new SoundLoader(path), new HighScoreLoader(path));
    }

    protected ResourceLoader(Path path, SharewareDownloader downloader, SpriteLoader spriteLoader, LevelLoader levelLoader,
                             SoundLoader soundLoader, HighScoreLoader highScoreLoader) {
        this.path = path;
        this.downloader = downloader;
        this.spriteLoader = spriteLoader;
        this.levelLoader = levelLoader;
        this.soundLoader = soundLoader;
        this.highScoreLoader = highScoreLoader;
    }

    public void ensureResourcesExist() {
        if (isEmptyDirectory(path)) {
            downloader.download();
        }
    }

    public SpriteLoader getSpriteLoader() {
        return spriteLoader;
    }

    public LevelLoader getLevelLoader() {
        return levelLoader;
    }

    public SoundLoader getSoundLoader() {
        return soundLoader;
    }

    public HighScoreLoader getHighScoreLoader() {
        return highScoreLoader;
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

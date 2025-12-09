package duke;

import duke.resources.ResourceLoader;
import duke.ui.DukeNukemFrame;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DukeNukem {
    private ResourceLoader resourceLoader;
    private GameLoop gameLoop;

    public DukeNukem(ResourceLoader resourceLoader, Renderer renderer) {
        this(resourceLoader, new GameLoop(renderer));
    }

    protected DukeNukem(ResourceLoader resourceLoader, GameLoop gameLoop) {
        this.resourceLoader = resourceLoader;
        this.gameLoop = gameLoop;
    }

    public void start() {
        resourceLoader.ensureResourcesExist();
        gameLoop.start();
    }

    public static void main(String... parameters) {
        Path basePath = Paths.get(".dn1"); // TODO read from config or parameters

        DukeNukemFrame frame = new DukeNukemFrame();
        ResourceLoader resourceLoader = new ResourceLoader(basePath);

        DukeNukem dukeNukem = new DukeNukem(resourceLoader, frame.getRenderer());
        dukeNukem.start();
    }
}

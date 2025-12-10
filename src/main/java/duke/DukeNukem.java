package duke;

import duke.resources.ResourceLoader;
import duke.ui.DukeNukemFrame;
import duke.ui.KeyHandler;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class DukeNukem {
    private ResourceLoader resourceLoader;
    private GameLoop gameLoop;

    private ScheduledExecutorService executor;

    public DukeNukem(ResourceLoader resourceLoader, GameLoop gameLoop) {
        this.resourceLoader = resourceLoader;
        this.gameLoop = gameLoop;
        this.executor = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());
    }

    public void start() {
        resourceLoader.ensureResourcesExist();

        executor.scheduleAtFixedRate(() -> gameLoop.tick(), 0L, 1L, MILLISECONDS);
    }

    public void stop() {
        executor.shutdownNow();
    }

    public static void main(String... parameters) {
        Path basePath = Paths.get(".dn1"); // TODO read from config or parameters

        KeyHandler keyHandler = new KeyHandler();
        DukeNukemFrame frame = new DukeNukemFrame(keyHandler);
        ResourceLoader resourceLoader = new ResourceLoader(basePath);

        DukeNukem dukeNukem = new DukeNukem(resourceLoader, new GameLoop(frame.getRenderer()));
        dukeNukem.start();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dukeNukem.stop();
            }
        });
    }
}

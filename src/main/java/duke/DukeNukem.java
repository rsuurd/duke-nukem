package duke;

import duke.dialog.DialogManager;
import duke.gfx.EgaPalette;
import duke.gfx.Font;
import duke.resources.AssetManager;
import duke.resources.ResourceLoader;
import duke.sfx.PcSpeaker;
import duke.sfx.SoundManager;
import duke.state.GameState;
import duke.state.GameplayState;
import duke.state.MainMenu;
import duke.state.StateManager;
import duke.ui.CanvasRenderer;
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
    private GameLoop gameLoop;

    private ScheduledExecutorService executor;

    public DukeNukem(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        this.executor = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());
    }

    public void start() {
        executor.scheduleAtFixedRate(() -> {
            try {
                gameLoop.tick();
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }, 0L, 10L, MILLISECONDS);
    }

    public void stop() {
        executor.shutdownNow();
    }

    public static void main(String... parameters) {
        Path basePath = Paths.get(".dn1"); // TODO read from config or parameters
        ResourceLoader resourceLoader = new ResourceLoader(basePath);
        AssetManager assets = new AssetManager(resourceLoader);
        assets.load();

        KeyHandler keyHandler = new KeyHandler();
        EgaPalette palette = new EgaPalette();
        PcSpeaker pcSpeaker = new PcSpeaker();
        SoundManager sounds = new SoundManager(assets, pcSpeaker);
        pcSpeaker.init();
        DialogManager dialogManager = new DialogManager(assets, new Font(assets));

        CanvasRenderer renderer = new CanvasRenderer(palette);
        GameContext context = new GameContext(assets, renderer, palette, keyHandler, sounds, dialogManager);
        DukeNukemFrame frame = new DukeNukemFrame(renderer, keyHandler);
        GameplayState state = new GameplayState(context);
//        GameState state = new MainMenu();
        StateManager manager = new StateManager(context, state);
        GameLoop gameLoop = new GameLoop(context, manager);

        DukeNukem dukeNukem = new DukeNukem(gameLoop);
        dukeNukem.start();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dukeNukem.stop();
            }
        });
    }
}

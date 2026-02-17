package duke;

import duke.dialog.DialogManager;
import duke.gameplay.Cheats;
import duke.gfx.EgaPalette;
import duke.gfx.Font;
import duke.resources.AssetManager;
import duke.resources.ResourceLoader;
import duke.sfx.PcSpeaker;
import duke.sfx.SoundManager;
import duke.state.GameplayState;
import duke.state.Introduction;
import duke.state.Prologue;
import duke.state.StateManager;
import duke.ui.CanvasRenderer;
import duke.ui.DukeNukemFrame;
import duke.ui.KeyHandler;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

                System.exit(-1);
            }
        }, 0L, 10L, MILLISECONDS);
    }

    public void stop() {
        executor.shutdownNow();
    }

    public static void main(String... parameters) {
        GameParameters gameParameters = GameParameters.parse(parameters);

        ResourceLoader resourceLoader = new ResourceLoader(gameParameters.path());
        AssetManager assets = new AssetManager(resourceLoader);
        assets.load();

        KeyHandler keyHandler = new KeyHandler();
        EgaPalette palette = new EgaPalette();
        PcSpeaker pcSpeaker = new PcSpeaker();
        SoundManager sounds = new SoundManager(assets, pcSpeaker);
        pcSpeaker.init();
        StateManager stateManager = new StateManager();
        DialogManager dialogManager = new DialogManager(assets, new Font(assets));


        CanvasRenderer renderer = new CanvasRenderer(palette);
        DukeNukemFrame frame = new DukeNukemFrame(renderer, keyHandler);
        GameSystems systems = new GameSystems(assets, renderer, palette, keyHandler, sounds, stateManager, dialogManager);
        GameplayState state = new GameplayState(systems, new Cheats(gameParameters.asp()));
//        GameState state = new MainMenu();
        GameLoop gameLoop = new GameLoop(systems, stateManager);
        DukeNukem dukeNukem = new DukeNukem(gameLoop);

        systems.requestState(new Introduction());
        dukeNukem.start();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dukeNukem.stop();
            }
        });
    }
}

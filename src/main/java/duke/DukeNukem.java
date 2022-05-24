package duke;

import javax.swing.*;
import java.nio.file.Paths;
import java.time.Duration;

public class DukeNukem {
    private static final long FPS = 16;
    private static final long TIME_STEP = Duration.ofSeconds(1).toNanos() / FPS;

    private ResourceLoader loader;
    private Gfx gfx;

    private GameState gameState;

    public static void main(String... parameters) {
        DukeNukem dukeNukem = new DukeNukem();

        JFrame frame = new JFrame("Duke Nukem");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(dukeNukem.gfx);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        dukeNukem.init();
        dukeNukem.loop();
    }

    public DukeNukem() {
        loader = new ResourceLoader(Paths.get(".dn1"));
        gfx = new Gfx(loader);
        gameState = new GameState();
    }

    public void init() {
        loader.init();
        gfx.init();
        gameState.switchLevel(loader.readLevel(1));
    }

    private void loop() {
        long lastUpdateTime = System.nanoTime();

        while (true) {
            long now = System.nanoTime();

            while ((now - lastUpdateTime) >= TIME_STEP) {
                // handle input
                update();
                gfx.render(gameState);

                lastUpdateTime += TIME_STEP;
            }

            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {}
        }
    }

    private void update() {
        gameState.moveCamera(4, 4);
    }
}

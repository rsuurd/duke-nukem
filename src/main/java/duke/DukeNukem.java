package duke;

import javax.swing.*;
import java.nio.file.Paths;
import java.time.Duration;

import static duke.Gfx.TILE_SIZE;

public class DukeNukem {
    private static final long FPS = 16;
    private static final long TIME_STEP = Duration.ofSeconds(1).toNanos() / FPS;

    private static final int CAMERA_SPEED = TILE_SIZE / 2;

    private ResourceLoader loader;
    private Gfx gfx;
    private KeyHandler keyHandler;

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
        keyHandler = new KeyHandler();
        gameState = new GameState();
    }

    public void init() {
        loader.init();
        gfx.init();
        gfx.addKeyListener(keyHandler);
        gfx.requestFocus();
        gameState.switchLevel(loader.readLevel(1));
    }

    private void loop() {
        long lastUpdateTime = System.nanoTime();

        while (true) {
            long now = System.nanoTime();

            while ((now - lastUpdateTime) >= TIME_STEP) {
                handleInput();

                gameState.update();
                gfx.render(gameState);

                lastUpdateTime += TIME_STEP;
            }

            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
            }
        }
    }

    private void handleInput() {
        Duke duke = gameState.getDuke();

        if (keyHandler.isLeft()) {
            duke.move(Facing.LEFT);
        } else if (keyHandler.isRight()) {
            duke.move(Facing.RIGHT);
        } else {
            duke.stopMove();
        }

        if (keyHandler.isJump()) {
            duke.jump();
        }
    }
}

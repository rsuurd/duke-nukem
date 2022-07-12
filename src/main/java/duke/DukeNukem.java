package duke;

import duke.modals.GameMenu;
import duke.modals.Message;
import duke.sounds.Sfx;

import javax.swing.*;
import java.nio.file.Paths;
import java.time.Duration;

import static duke.Gfx.TILE_SIZE;
import static java.awt.event.KeyEvent.VK_F1;
import static java.awt.event.KeyEvent.VK_S;

public class DukeNukem {
    private static final long FPS = 16;
    private static final long TIME_STEP = Duration.ofSeconds(1).toNanos() / FPS;

    private ResourceLoader loader;
    private Gfx gfx;
    private Sfx sfx;
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
        sfx = new Sfx(loader.readSounds());
        keyHandler = new KeyHandler();
        gameState = new GameState(loader, sfx);
    }

    public void init() {
        loader.init();
        gfx.init();
        gfx.addKeyListener(keyHandler);
        gfx.requestFocus();
        sfx.init();
        gameState.switchLevel(loader.readLevel(1, 3));
    }

    private void loop() {
        long lastUpdateTime = System.nanoTime();

        while (true) {
            long now = System.nanoTime();

            if (gameState.getModals().isEmpty()) {
                while ((now - lastUpdateTime) >= TIME_STEP) {
                    handleInput();

                    gameState.update();
                    gfx.render(gameState);

                    lastUpdateTime += TIME_STEP;
                }
            } else {
                gfx.render(gameState.getModals());

                gameState.getModals().element().handleInput(gameState, keyHandler);
                keyHandler.clear();

                lastUpdateTime = now;
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

        duke.fire(keyHandler.isFire());
        duke.setUsing(keyHandler.isUsing());

        if (keyHandler.isPressed(VK_F1)) {
            gameState.showModal(new GameMenu());
        }

        if (keyHandler.isPressed(VK_S)) {
            sfx.toggle();
            gameState.showModal(new Message(TILE_SIZE, 48, "      Sound toggle\n\n    The sound is " + (sfx.isEnabled() ? "ON" : "OFF"), true));
        }
    }
}

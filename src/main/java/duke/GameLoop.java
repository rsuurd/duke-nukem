package duke;

import java.time.Duration;

public class GameLoop {
    private static final int UPDATES_PER_SECOND = 16;
    private static final long TIME_STEP = Duration.ofSeconds(1).toNanos() / UPDATES_PER_SECOND;
    private static final long MILLI = Duration.ofMillis(1).toNanos();

    private boolean running;

    private Renderer renderer;

    public GameLoop(Renderer renderer) {
        this.renderer = renderer;
    }

    public void start() {
        running = true;

        loop();
    }

    public void stop() {
        running = false;
    }

    private void loop() {
        long nextUpdate = System.nanoTime();

        while (running) {
            long now = System.nanoTime();

            handleInput();

            if (now >= nextUpdate) {
                update();
                render();

                nextUpdate = calculateNext(now, nextUpdate);
            }

            sleep(nextUpdate);
        }
    }

    private void handleInput() {
    }

    private void update() {
    }

    private void render() {
        renderer.clear();
        renderer.flip();
    }

    private long calculateNext(long now, long lastUpdate) {
        long nextUpdate = lastUpdate + TIME_STEP;
        long behind = now - nextUpdate;

        // If we fell behind by one or more ticks, reset
        if (behind >= TIME_STEP) {
            nextUpdate = now + TIME_STEP;
        }

        return nextUpdate;
    }

    private void sleep(long nextUpdate) {
        long waitTime = nextUpdate - System.nanoTime();
        if (waitTime > MILLI * 2) {
            try {
                Thread.sleep(waitTime / MILLI, (int) (waitTime % MILLI));
            } catch (InterruptedException e) {}
        } else {
            Thread.onSpinWait();
        }
    }
}

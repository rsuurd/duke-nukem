package duke;

import java.time.Duration;

public class GameLoop {
    private static final int UPDATES_PER_SECOND = 16;
    private static final long TIME_STEP = Duration.ofSeconds(1).toNanos() / UPDATES_PER_SECOND;

    private GameContext context;

    private long nextUpdate;

    public GameLoop(GameContext context) {
        this.context = context;

        nextUpdate = now();
    }

    public void tick() {
        long now = now();

        handleInput();

        if (now >= nextUpdate) {
            update();
            render();

            nextUpdate = calculateNext(now, nextUpdate);
        }
    }

    private long now() {
        return System.nanoTime();
    }

    private void handleInput() {
    }

    private void update() {
        context.getGameState().update(context);
    }

    private void render() {
        context.getRenderer().clear();
        context.getGameState().render(context);
        context.getRenderer().flip();
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
}

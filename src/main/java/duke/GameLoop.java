package duke;

import duke.state.StateManager;

import java.time.Duration;

public class GameLoop {
    private static final int UPDATES_PER_SECOND = 16;
    private static final long TIME_STEP = Duration.ofSeconds(1).toNanos() / UPDATES_PER_SECOND;

    private GameContext context;
    private StateManager manager;

    private long nextUpdate;

    public GameLoop(GameContext context, StateManager manager) {
        this.context = context;
        this.manager = manager;

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
        manager.update();
    }

    private void render() {
        context.getRenderer().clear();
        manager.render();
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

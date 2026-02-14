package duke.gameplay.player;

import duke.level.Level;

public class FallHandler {
    private int fallTicks;

    public void slowFall() {
        fallTicks = SLOW_FALL_TICKS;
    }

    public void fall() {
        fallTicks = FALL_TICKS;
    }

    public void update(Player player) {
        if (player.getState() != State.FALLING) return;

        fallTicks = Math.max(fallTicks - 1, 0);
    }

    public int getVerticalAcceleration() {
        return Player.SPEED;
    }

    public int getTerminalVelocity() {
        return fallTicks > 0 ? Player.SPEED : Level.TILE_SIZE;
    }

    static final int SLOW_FALL_TICKS = 8;
    static final int FALL_TICKS = 2;
}

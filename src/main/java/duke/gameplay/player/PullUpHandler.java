package duke.gameplay.player;

import duke.level.Level;

public class PullUpHandler {
    private int timer;

    public void beginPullUp() {
        timer = 0;
    }

    public void update(Player player) {
        if (player.getState() != State.PULLING_UP) return;

        if (timer++ < PULL_UP_DURATION) {
            player.setY(player.getY() - PULL_UP_OFFSET);
        } else {
            player.pullUpComplete();
        }
    }

    static final int PULL_UP_OFFSET = Level.TILE_SIZE;
    static final int PULL_UP_DURATION = 3;
}

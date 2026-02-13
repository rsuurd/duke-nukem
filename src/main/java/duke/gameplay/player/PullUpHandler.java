package duke.gameplay.player;

public class PullUpHandler {
    private int timer;

    public void beginPullUp() {
        timer = 0;
    }

    public void update(Player player) {
        if (player.getState() != State.PULLING_UP) return;

        player.setY(player.getY() - PULL_UP_OFFSETS[timer++]);

        if (timer >= PULL_UP_OFFSETS.length) {
            player.pullUpComplete();
        }
    }

    static final int[] PULL_UP_OFFSETS = {16, 8, 24};
}

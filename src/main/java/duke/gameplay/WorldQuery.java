package duke.gameplay;

import duke.gameplay.player.Player;

public interface WorldQuery {
    boolean isSolid(int row, int col);

    int getTileFlags(int row, int col);

    Player getPlayer();
}

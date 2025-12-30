package duke.gameplay;

import duke.level.Level;

import java.util.LinkedList;
import java.util.List;

public class GameplayContext {
    private final Player player;
    private final Level level;
    private final List<Active> actives;

    public GameplayContext(Player player, Level level) {
        this.player = player;
        this.level = level;

        actives = new LinkedList<>();
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public List<Active> getActives() {
        return actives;
    }

    public void spawn(Active active) {
        actives.add(active);
    }
}

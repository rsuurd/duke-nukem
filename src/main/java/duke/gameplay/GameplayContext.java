package duke.gameplay;

import duke.level.Level;

import java.util.LinkedList;
import java.util.List;

public class GameplayContext {
    private Player player;
    private Level level;
    private List<Active> actives;
    private List<Active> spawns;

    public GameplayContext(Player player, Level level) {
        this.player = player;
        this.level = level;

        actives = new LinkedList<>();
        spawns = new LinkedList<>();
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
        spawns.add(active);
    }

    public void flushSpawns() {
        actives.addAll(spawns);
        spawns.clear();
    }
}

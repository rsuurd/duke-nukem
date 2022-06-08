package duke;

import duke.active.Active;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameState {
    private Duke duke;
    private Level level;

    private int score;

    private List<Bolt> bolts;
    private List<Active> spawns;

    public GameState() {
        score = 0;

        bolts = new ArrayList<>();
        spawns = new ArrayList<>();
    }

    public void switchLevel(Level level) {
        duke = new Duke();

        this.level = level;

        duke.reset(level);
    }

    public Level getLevel() {
        return level;
    }

    public Duke getDuke() {
        return duke;
    }

    public List<Bolt> getBolts() {
        return bolts;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int points) {
        score += points;
    }

    public void update() {
        level.update(this);

        Iterator<Bolt> boltIterator = getBolts().iterator();

        while (boltIterator.hasNext()) {
            Bolt bolt = boltIterator.next();

            if (bolt.isActive()) {
                bolt.update(this);
            }

            if (!bolt.isActive()) {
                boltIterator.remove();
            }
        }

        level.getActives().addAll(spawns);
        spawns.clear();

        duke.update(this);
    }

    public void spawn(Active active) {
        spawns.add(active);
    }
}

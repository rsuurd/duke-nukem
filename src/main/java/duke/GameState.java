package duke;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameState {
    private Duke duke;
    private Level level;

    private int score;

    private List<Bolt> bolts;

    public GameState() {
        score = 0;

        bolts = new ArrayList<>();
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

        duke.update(this);
    }
}

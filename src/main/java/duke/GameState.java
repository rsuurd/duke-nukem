package duke;

import duke.active.Active;
import duke.effects.Effect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameState {
    private ResourceLoader loader;

    private Duke duke;
    private Inventory inventory;

    private Level level;

    private int score;

    private List<Bolt> bolts;
    private List<Active> spawns;
    private List<Effect> effects;

    public GameState(ResourceLoader loader) {
        this.loader = loader;

        duke = new Duke();
        inventory = new Inventory();

        score = 0;

        bolts = new ArrayList<>();
        spawns = new ArrayList<>();
        effects = new ArrayList<>();
    }

    public void resetLevel() {
        score = 0;
        switchLevel(loader.readLevel(level.getNumber()));
    }

    public void switchLevel(Level level) {
        this.level = level;

        duke.reset(level);
    }

    public Level getLevel() {
        return level;
    }

    public Duke getDuke() {
        return duke;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Bolt> getBolts() {
        return bolts;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int points) {
        score += points;
    }

    public void update() {
        duke.update(this);

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

        level.update(this);

        getEffects().removeIf(Effect::isDone);
        level.getActives().addAll(spawns);
        spawns.clear();

        if (duke.getHealth() < 0) {
            resetLevel();
        }
    }

    public void spawn(Active active) {
        spawns.add(active);
    }

    public void addEffect(Effect effect) {
        this.effects.add(effect);
    }
}

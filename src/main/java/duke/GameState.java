package duke;

import duke.active.Active;
import duke.effects.Effect;
import duke.modals.Modal;

import java.util.*;

import static duke.Gfx.TILE_SIZE;

public class GameState {
    private ResourceLoader loader;

    private Duke duke;
    private Inventory inventory;

    private Level level;

    private int score;

    private List<Bolt> bolts;
    private List<Active> spawns;
    private List<Effect> effects;

    private Bonus bonus;

    private Hints hints;

    private Deque<Modal> modals;

    public GameState(ResourceLoader loader) {
        this.loader = loader;

        duke = new Duke();
        inventory = new Inventory();

        score = 0;

        bonus = new Bonus(this);
        hints = new Hints(this);

        modals = new ArrayDeque<>();
    }

    public void resetLevel() {
        score = 0;
        switchLevel(loader.readLevel(level.getNumber(), level.getNext()));
        bonus.reset();
        closeModals();
    }

    public void switchLevel(Level level) {
        this.level = level;

        duke.reset(level);

        bolts = new ArrayList<>();
        spawns = new ArrayList<>();
        effects = new ArrayList<>();
    }

    public void goToNextLevel() {
        if (level.getNumber() == 2) { // intermission hallway
            switchLevel(loader.readLevel(level.getNext(), level.getNext() + 1));
        } else {
            switchLevel(loader.readLevel(2, level.getNext()));
            int y = duke.getY() + 100;

            for (Bonus.Type type : Bonus.Type.values()) {
                if (bonus.isEarned(type)) {
                    increaseScore(10000);
                    addEffect(new Effect.Bonus(duke.getX(), y, type));

                    y += 2 * TILE_SIZE;
                }
            }
        }

        bonus.reset();
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

    public Bonus getBonus() {
        return bonus;
    }

    public Hints getHints() {
        return hints;
    }

    public void showModal(Modal modal) {
        modals.push(modal);
    }

    public void closeModals() {
        modals.clear();
    }

    public Deque<Modal> getModals() {
        return modals;
    }
}

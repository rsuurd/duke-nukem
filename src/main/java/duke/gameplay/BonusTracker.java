package duke.gameplay;

import duke.DukeNukemException;
import duke.gameplay.active.Acme;
import duke.gameplay.active.SecurityCamera;
import duke.gameplay.effects.Bonus;
import duke.gameplay.player.Player;
import duke.level.Level;

import java.util.EnumMap;
import java.util.Map;

import static duke.level.Level.TILE_SIZE;

public class BonusTracker {
    private boolean damageTaken;
    private StringBuilder characters;

    private Map<Type, DestructionCounter> destructionCounters;

    public BonusTracker() {
        destructionCounters = new EnumMap<>(Type.class);
        characters = new StringBuilder();
    }

    public void reset(Level level) {
        damageTaken = false;
        characters.delete(0, characters.length());
        destructionCounters.clear();
        destructionCounters.put(Type.CAMERAS, new DestructionCounter(count(level, SecurityCamera.class)));
        destructionCounters.put(Type.ACME, new DestructionCounter(count(level, Acme.class)));
    }

    private int count(Level level, Class<? extends Active> clazz) {
        if (level == null) return 0;

        return (int) level.getActives().stream().filter(clazz::isInstance).count();
    }

    public void trackDestroyed(Type type) {
        if (type == Type.HEALTH || type == Type.DUKE) {
            throw new DukeNukemException("Cannot track destruction of " + type);
        }

        if (destructionCounters.containsKey(type)) {
            destructionCounters.get(type).increment();
        }
    }

    public void damageTaken() {
        damageTaken = true;
    }

    public boolean trackDUKE(char c) {
        characters.append(c);

        return isEarned(Type.DUKE);
    }

    boolean isEarned(Type type) {
        return switch (type) {
            case HEALTH -> !damageTaken;
            case DUKE -> characters.toString().equals("DUKE");
            default -> destructionCounters.containsKey(type) && destructionCounters.get(type).isCompleted();
        };
    }

    public void reward(GameplayContext context) {
        if (!context.getLevel().getDescriptor().isHallway()) return; // Only reward in hallways

        Player player = context.getPlayer();
        int x = player.getX();
        int y = player.getY() + 100;

        for (Type type : Type.values()) {
            if (isEarned(type)) {
                context.getScoreManager().score(BONUS);
                context.getActiveManager().spawn(new Bonus(x, y, type));

                y += 2 * TILE_SIZE;
            }
        }
    }

    public enum Type {
        //Destroying all cameras in a level
        CAMERAS,
        //Not losing any health in a level
        HEALTH,
        //Making all of the ACME signs fall
        ACME,
        //Destroying all missiles in a level
        MISSILES,
        //Collecting the letters D, U, K, and E in the correct order
        DUKE,
        //Destroying all of the Snake Techbots
        SNAKE,
        //Destroying all of the Bunny Techbots
        BUNNY
    }

    private static class DestructionCounter {
        private int total;
        private int destroyed;

        public DestructionCounter(int total) {
            reset(total);
        }

        public void reset(int total) {
            this.total = total;
            destroyed = 0;
        }

        public void increment() {
            destroyed++;
        }

        public boolean isCompleted() {
            return total > 0 && destroyed >= total;
        }
    }

    private static final int BONUS = 10000;
}

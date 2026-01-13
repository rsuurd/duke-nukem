package duke.gameplay.player;

import duke.gameplay.Damaging;
import duke.gameplay.GameplayContext;

public class Health {
    private int current;
    private boolean damageTaken;
    private int invulnerability;

    public Health() {
        this(MAX_HEALTH, false, 0);
    }

    Health(int current, boolean damageTaken, int invulnerability) {
        this.current = current;
        this.damageTaken = damageTaken;
        this.invulnerability = invulnerability;
    }

    public int getCurrent() {
        return current;
    }

    public boolean isDamageTaken() {
        return damageTaken;
    }

    public boolean isInvulnerable() {
        return invulnerability > 0;
    }

    public int getInvulnerability() {
        return invulnerability;
    }

    public void update(GameplayContext context) {
        if (isInvulnerable()) {
            invulnerability--;
        }

        damageTaken = false;
    }

    public void takeDamage(Damaging damaging) {
        if (isInvulnerable()) return;

        current = Math.max(current - damaging.getDamage(), 0);
        invulnerability = INVULNERABILITY_FRAMES;
        damageTaken = true;
    }

    public void increaseHealth(int amount) {
        current = Math.min(current + amount, MAX_HEALTH);
    }

    public static final int MAX_HEALTH = 8;
    static final int INVULNERABILITY_FRAMES = 16;
}

package duke.gameplay.player;

import duke.gameplay.GameplayContext;
import duke.gameplay.Health;

public class PlayerHealth extends Health {
    private boolean damageTaken;
    private int invulnerability;

    public PlayerHealth() {
        this(MAX_HEALTH, false, 0);
    }

    PlayerHealth(int current, boolean damageTaken, int invulnerability) {
        super(current);

        this.damageTaken = damageTaken;
        this.invulnerability = invulnerability;
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

    public void takeDamage(int amount) {
        if (isInvulnerable()) return;

        super.takeDamage(amount);

        grantInvulnerability();
        damageTaken = true;
    }

    public void grantInvulnerability() {
        invulnerability = INVULNERABILITY_FRAMES;
    }

    public static final int MAX_HEALTH = 8;
    static final int INVULNERABILITY_FRAMES = 16;
}

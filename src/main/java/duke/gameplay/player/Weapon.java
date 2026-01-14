package duke.gameplay.player;

import duke.gameplay.Bolt;
import duke.gameplay.BoltManager;
import duke.gameplay.GameplayContext;
import duke.sfx.Sfx;

public class Weapon {
    private int firepower;
    private boolean triggered;
    private boolean ready;

    public Weapon() {
        this(STARTING_FIREPOWER, false, true);
    }

    Weapon(int firepower, boolean triggered, boolean ready) {
        this.firepower = firepower;
        this.triggered = triggered;
        this.ready = ready;
    }

    public int getFirepower() {
        return firepower;
    }

    public void increaseFirepower() {
        firepower = Math.min(firepower + 1, MAX_FIREPOWER);
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public boolean isReady() {
        return ready;
    }

    public void fire(GameplayContext context) {
        BoltManager boltManager = context.getBoltManager();

        if (isTriggered() && isReady() && boltManager.countBolts() < firepower) {
            boltManager.spawn(Bolt.create(context.getPlayer()));
            context.getSoundManager().play(Sfx.PLAYER_GUN);
        }

        ready = !triggered;
    }

    static final int STARTING_FIREPOWER = 1;
    static final int MAX_FIREPOWER = 4;
}

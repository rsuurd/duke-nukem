package duke;

import duke.active.Acme;
import duke.active.Active;
import duke.active.Camera;

import java.util.HashSet;
import java.util.Set;

public class Bonus {
    private GameState state;

    private Set<Type> earned;
    private StringBuilder letters;

    public Bonus(GameState state) {
        this.state = state;

        earned = new HashSet<>();
        reset();
    }

    public void reset() {
        earned.clear();
        earned.add(Type.HEALTH);

        letters = new StringBuilder();
    }

    public void lostHealth() {
        earned.remove(Type.HEALTH);
    }

    public void cameraDestroyed() {
        if (noneActive(Camera.class)) {
            earned.add(Type.CAMERAS);
        }
    }

    public void acmeDestroyed() {
        if (noneActive(Acme.class)) {
            earned.add(Type.ACME);
        }
    }

    private boolean noneActive(Class<? extends Active> activeType) {
        return state.getLevel().getActives().stream().noneMatch(active -> active.getClass() == activeType && active.isActive());
    }

    public void addLetter(char letter) {
        letters.append(letter);

        if ("DUKE".equals(letters.toString())) {
            earned.add(Type.DUKE);
        }
    }

    public boolean isEarned(Type type) {
        return earned.contains(type);
    }

    public enum Type {
        CAMERAS,
        HEALTH,
        ACME,
        MISSILES,
        DUKE,
        SNAKE,
        BUNNY
    }
}

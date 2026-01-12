package duke.gameplay;

public interface Damaging {
    default int getDamage() {
        return 1;
    }
}

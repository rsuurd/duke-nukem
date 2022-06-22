package duke;

import duke.active.Key;

import java.util.HashSet;
import java.util.Set;

public class Inventory {
    private Set<Key.Type> keyRing;

    public Inventory() {
        keyRing = new HashSet<>();
    }

    public boolean hasKey(Key.Type type) {
        return keyRing.contains(type);
    }

    public boolean addKey(Key.Type type) {
        return keyRing.add(type);
    }

    public boolean useKey(Key.Type type) {
        return keyRing.remove(type);
    }
}

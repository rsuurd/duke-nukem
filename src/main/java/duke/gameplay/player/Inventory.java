package duke.gameplay.player;

import duke.gameplay.active.items.Key;

import java.util.HashSet;
import java.util.Set;

public class Inventory {
    private Set<Key.Type> keys = new HashSet<>();

    public void addKey(Key.Type keyType) {
        keys.add(keyType);
    }

    public boolean hasKey(Key.Type keyType) {
        return keys.contains(keyType);
    }

    public boolean useKey(Key.Type keyType) {
        return keys.remove(keyType);
    }
}

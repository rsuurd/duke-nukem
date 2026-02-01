package duke.gameplay.player;

import duke.gameplay.active.items.Key;

import java.util.HashSet;
import java.util.Set;

public class Inventory {
    private Set<Key.Type> keys = new HashSet<>();
    private Set<Equipment> equipment = new HashSet<>();

    public void addKey(Key.Type keyType) {
        keys.add(keyType);
    }

    public boolean hasKey(Key.Type keyType) {
        return keys.contains(keyType);
    }

    public boolean useKey(Key.Type keyType) {
        return keys.remove(keyType);
    }

    public void addEquipment(Equipment equipment) {
        this.equipment.add(equipment);
    }

    public boolean isEquippedWith(Equipment equipment) {
        return this.equipment.contains(equipment);
    }

    public boolean removeEquipment(Equipment equipment) {
        return this.equipment.remove(equipment);
    }

    public enum Equipment {
        BOOTS,
        ROBOHAND,
        GRAPPLING_HOOKS,
        ACCESS_CARD
    }
}

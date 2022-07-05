package duke;

import duke.active.*;

import java.util.HashMap;
import java.util.Map;

public class Hints {
    private Map<Class<? extends Active>, String> hints;

    private String current;

    public Hints() {
        hints = new HashMap<>();

        hints.put(Elevator.class, "Secret tip: Press the UP\nARROW to activate the\nELEVATORS.\n      Press ENTER:");
        hints.put(Exit.class, "Secret tip: Press the UP\nARROW to open the DOOR.\n\n      Press ENTER:");
        hints.put(Lock.class, "Secret tip: Find the key\nthat goes here and press\nthe UP ARROW.\n      Press ENTER:");
        hints.put(Key.class, "Secret tip: You need the\ncorrect key before\nyou can use this lock.\n      Press ENTER:");
        hints.put(NuclearMolecule.class, "Secret tip: The nuclear\nmolecule increases\nyour health to maximum!\n      Press ENTER:");
        hints.put(Soda.class, "Secret tip: Drink soda\nto increase your health!\n\n      Press ENTER:");
        hints.put(Turkey.class, "Secret tip: Eat turkey\nto increase your health!\n\n      Press ENTER:");
    }

    public void showHint(Active source) {
        showHint(source.getClass());
    }

    public void showHint(Class<? extends Active> source) {
        if (hints.containsKey(source)) {
            current = hints.remove(source);

            // TODO fix me
            hints.put(Key.class, "Secret tip: You need the\ncorrect key before\nyou can use this lock.\n      Press ENTER:");
        }
    }

    public String getCurrent() {
        return current;
    }

    public void clear() {
        current = null;
    }
}

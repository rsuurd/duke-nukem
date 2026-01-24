package duke.dialog;

import duke.gameplay.GameplayContext;
import duke.sfx.Sfx;

import java.util.EnumMap;
import java.util.Map;

public class Hints {
    private EnumMap<Type, String> hints;

    public Hints() {
        hints = new EnumMap<>(Map.of(
                Type.NOTES, MESSAGE_TIP,
                Type.ELEVATOR, ELEVATOR_TIP,
                Type.EXIT, EXIT_TIP,
                Type.LOCK, LOCK_TIP,
                Type.NUCLEAR_MOLECULE, NUCLEAR_MOLECULE_TIP,
                Type.SODA, SODA_TIP,
                Type.TURKEY, TURKEY_TIP
        ));
    }

    Hints(EnumMap<Type, String> hints) {
        this.hints = hints;
    }

    public void showHint(Type type, GameplayContext context) {
        String hint = hints.remove(type);

        if (hint != null) {
            context.getSoundManager().play(Sfx.CHEAT_MODE);
            context.getDialogManager().open(Dialog.hint(SECRET_TIP + hint));
        }
    }

    public enum Type {
        NOTES,
        ELEVATOR,
        EXIT,
        LOCK,
        KEY,
        NUCLEAR_MOLECULE,
        SODA,
        TURKEY
    }

    private static final String SECRET_TIP = "Secret tip: ";
    private static final String MESSAGE_TIP = "Press the UP\nARROW to read the notes.\n";
    private static final String ELEVATOR_TIP = "Press the UP\nARROW to activate the\nELEVATORS.";
    private static final String EXIT_TIP = "Press the UP\nARROW to open the DOOR.\n";
    private static final String LOCK_TIP = "Find the key\nthat goes here and press\nthe UP ARROW.";
    private static final String NUCLEAR_MOLECULE_TIP = "The nuclear\nmolecule increases\nyour health to maximum!";
    private static final String SODA_TIP = "Drink soda\nto increase your health!\n";
    private static final String TURKEY_TIP = "Eat turkey\nto increase your health!\n";
}

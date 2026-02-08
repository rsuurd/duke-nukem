package duke.dialog;

import duke.gameplay.GameplayContext;
import duke.sfx.Sfx;

import java.util.HashSet;
import java.util.Set;

public class Hints {
    private Set<Hint> hints;

    private boolean enabled;

    public Hints() {
        hints = new HashSet<>(Set.of(Hint.values()));

        enabled = true;
    }

    public void toggle() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void showHint(Hint hint, GameplayContext context) {
        if (!enabled) return;

        if (hints.remove(hint)) {
            context.getSoundManager().play(Sfx.CHEAT_MODE);
            context.getDialogManager().open(Dialog.hint(SECRET_TIP + hint.text));
        }
    }

    public enum Hint {
        NOTES(MESSAGE_TIP),
        ELEVATOR(ELEVATOR_TIP),
        EXIT(EXIT_TIP),
        LOCK(LOCK_TIP),
        KEY(LOCK_TIP),
        NUCLEAR_MOLECULE(NUCLEAR_MOLECULE_TIP),
        SODA(SODA_TIP),
        TURKEY(TURKEY_TIP),
        TRANSPORTER(TRANSPORTER_TIP),
        ACCESS_CARD(ACCESS_CARD_TIP),
        ROBOHAND(ROBOHAND_TIP);

        private String text;

        Hint(String text) {
            this.text = text;
        }
    }

    private static final String SECRET_TIP = "Secret tip: ";
    private static final String MESSAGE_TIP = "Press the UP\nARROW to read the notes.\n";
    private static final String ELEVATOR_TIP = "Press the UP\nARROW to activate the\nELEVATORS.";
    private static final String EXIT_TIP = "Press the UP\nARROW to open the DOOR.\n";
    private static final String LOCK_TIP = "Find the key\nthat goes here and press\nthe UP ARROW.";
    private static final String NUCLEAR_MOLECULE_TIP = "The nuclear\nmolecule increases\nyour health to maximum!";
    private static final String SODA_TIP = "Drink soda\nto increase your health!\n";
    private static final String TURKEY_TIP = "Eat turkey\nto increase your health!\n";
    private static final String TRANSPORTER_TIP = "Press the UP\nARROW to activate the\ntransporter.";
    private static final String ACCESS_CARD_TIP = "Find the\nACCESS CARD and press\nthe UP ARROW here.";
    private static final String ROBOHAND_TIP = "Press the UP\nARROW here after you\nhave found the ROBOHAND.";
}

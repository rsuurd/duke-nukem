package duke.state.storyboard;

import duke.dialog.Dialog;
import duke.gfx.Sprite;
import duke.sfx.Sfx;

public class Panel {
    private Sprite image;

    private Dialog dialog;
    private Sfx sfx;

    public Panel(Sprite image, Dialog dialog) {
        this(image, null, dialog);
    }

    public Panel(Sfx sfx, Dialog dialog) {
        this(null, sfx, dialog);
    }

    public Panel(Sprite image, Sfx sfx, Dialog dialog) {
        this.image = image;
        this.sfx = sfx;
        this.dialog = dialog;
    }

    public Sprite getImage() {
        return image;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public Sfx getSfx() {
        return sfx;
    }
}

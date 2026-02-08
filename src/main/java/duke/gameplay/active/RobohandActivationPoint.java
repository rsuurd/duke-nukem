package duke.gameplay.active;

import duke.dialog.Dialog;
import duke.dialog.Hints;
import duke.gameplay.*;
import duke.gameplay.player.Inventory;
import duke.gameplay.player.Player;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.sfx.Sfx;

import static duke.level.Level.TILE_SIZE;

public class RobohandActivationPoint extends Active implements Updatable, SpriteRenderable, Interactable {
    private Animation animation;

    private boolean activated;

    public RobohandActivationPoint(int x, int y) {
        super(x, y, TILE_SIZE, TILE_SIZE);

        animation = new Animation(DESCRIPTOR);
        activated = false;
    }

    @Override
    public boolean canInteract(Player player) {
        return player.intersects(this);
    }

    @Override
    public void interactRequested(GameplayContext context) {
        if (hasRobohand(context.getPlayer())) {
            extendBridge(context);
        } else {
            context.getSoundManager().play(Sfx.CHEAT_MODE); // TODO verify
            context.getDialogManager().open(Dialog.hint("Secret tip: You need the\nROBOHAND.")); // TODO check actual message
        }
    }

    private boolean hasRobohand(Player player) {
        return player.getInventory().isEquippedWith(Inventory.Equipment.ROBOHAND);
    }

    private void extendBridge(GameplayContext context) {
        if (activated) return;

        activated = true;

        for (Active active : context.getActiveManager().getActives()) {
            if (active instanceof Girder girder) {
                girder.extend(context);
            }
        }

        // this will make it stay lit up
        animation.reverse();
        animation.reset();
    }

    @Override
    public void update(GameplayContext context) {
        if (canInteract(context.getPlayer())) {
            context.getHints().showHint(Hints.Hint.ROBOHAND, context);
        }

        if (!activated) {
            animation.tick();
        }
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    @Override
    public Layer getLayer() {
        return Layer.BACKGROUND;
    }

    private static final SpriteDescriptor ROBOHAND = new SpriteDescriptor(SpriteDescriptor.OBJECTS, 114);
    private static final AnimationDescriptor DESCRIPTOR = new AnimationDescriptor(ROBOHAND, 2, 8);
}

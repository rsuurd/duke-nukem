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

public class AccessCardActivationPoint extends Active implements Updatable, SpriteRenderable, Interactable {
    private boolean forceFieldDeactivated;

    private Animation animation;

    public AccessCardActivationPoint(int x, int y) {
        super(x, y, TILE_SIZE, TILE_SIZE);

        forceFieldDeactivated = false;
        animation = new Animation(DESCRIPTOR);
    }

    @Override
    public boolean canInteract(Player player) {
        return player.intersects(this);
    }

    @Override
    public void interactRequested(GameplayContext context) {
        if (context.getPlayer().getInventory().removeEquipment(Inventory.Equipment.ACCESS_CARD)) {
            context.getActiveManager().getActives().removeIf(active -> active instanceof ForceField);
            forceFieldDeactivated = true;
        } else {
            context.getSoundManager().play(Sfx.CHEAT_MODE); // TODO verify
            context.getDialogManager().open(Dialog.hint("Secret tip: You need the\nACCESS CARD.")); // TODO check actual message
        }
    }

    @Override
    public void update(GameplayContext context) {
        if (forceFieldDeactivated) return;

        if (canInteract(context.getPlayer())) {
            context.getHints().showHint(Hints.Hint.ACCESS_CARD, context);
        }

        animation.tick();
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return forceFieldDeactivated ? ACCESS_CARD_INSERTED : animation.getSpriteDescriptor();
    }

    @Override
    public Layer getLayer() {
        return Layer.BACKGROUND;
    }

    boolean isForceFieldDeactivated() {
        return forceFieldDeactivated;
    }

    private static final AnimationDescriptor DESCRIPTOR = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.OBJECTS, 105), 8, 1);
    private static final SpriteDescriptor ACCESS_CARD_INSERTED = new SpriteDescriptor(SpriteDescriptor.OBJECTS, 113);
}

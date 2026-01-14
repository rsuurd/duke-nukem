package duke.gameplay.active;

import duke.gameplay.*;
import duke.gameplay.active.items.Key;
import duke.gameplay.player.Player;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.resources.AssetManager;

import java.util.List;

public class Lock extends Active implements Updatable, SpriteRenderable, Interactable {
    private Key.Type requiredKey;
    private boolean locked;
    private Animation animation;

    public Lock(int x, int y, Key.Type requiredKey) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        this.requiredKey = requiredKey;
        locked = true;
        animation = createAnimation(requiredKey);
    }

    boolean isLocked() {
        return locked;
    }

    @Override
    public boolean canInteract(Player player) {
        return locked && player.intersects(this) && player.getInventory().hasKey(requiredKey);
    }

    @Override
    public void interactRequested(GameplayContext context) {
        if (context.getPlayer().getInventory().useKey(requiredKey)) {
            locked = false;

            for (Active active : context.getActiveManager().getActives()) {
                if (active instanceof Door door && door.requiresKey(requiredKey)) {
                    door.open();
                }
            }
        }
    }

    @Override
    public void update(GameplayContext context) {
        if (locked) {
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

    private static Animation createAnimation(Key.Type requiredKey) {
        List<SpriteDescriptor> frames = List.of(createColoredLockDescriptor(requiredKey), LOCK_DESCRIPTOR);
        AnimationDescriptor descriptor = new AnimationDescriptor(frames, 4, AnimationDescriptor.Type.LOOP);

        return new Animation(descriptor);
    }

    private static SpriteDescriptor createColoredLockDescriptor(Key.Type requiredKey) {
        return LOCK_DESCRIPTOR.withBaseIndex(GFX_LOCK_INDEX + 1 + requiredKey.ordinal());
    }

    private static final int GFX_LOCK_INDEX = 136;
    private static final SpriteDescriptor LOCK_DESCRIPTOR = new SpriteDescriptor(AssetManager::getObjects, GFX_LOCK_INDEX, 0, 0, 1, 1);
}

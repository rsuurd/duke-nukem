package duke.gameplay.active;

import duke.gameplay.*;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.resources.AssetManager;

import java.util.List;

import static duke.level.Level.TILE_SIZE;

public class SecurityCamera extends Active implements Movable, Updatable, SpriteRenderable {
    private Animation animation;

    public SecurityCamera(int x, int y) {
        super(x, y, TILE_SIZE, TILE_SIZE);

        animation = new Animation(DESCRIPTORS.getFirst());
    }

    @Override
    public void update(GameplayContext context) {
        int col = context.getPlayer().getX() / TILE_SIZE;
        int index = Integer.signum(col - (getX() / TILE_SIZE)) + 1;

        animation.setAnimation(DESCRIPTORS.get(index));
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    @Override
    public int getBaseIndex() {
        return animation.getCurrentBaseIndex();
    }

    @Override
    public Layer getLayer() {
        return Layer.BACKGROUND;
    }

    private static List<AnimationDescriptor> DESCRIPTORS;

    static {
        SpriteDescriptor descriptor = new SpriteDescriptor(AssetManager::getAnim, 208, 0, 0, 1, 1);

        DESCRIPTORS = List.of(
                new AnimationDescriptor(descriptor, 1, 1),
                new AnimationDescriptor(descriptor.withBaseIndex(209), 1, 1),
                new AnimationDescriptor(descriptor.withBaseIndex(210), 1, 1)
        );
    }
}

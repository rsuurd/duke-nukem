package duke.gameplay.active;

import duke.gameplay.Active;
import duke.gameplay.Movable;
import duke.gameplay.SpriteRenderable;
import duke.gameplay.Updatable;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;

import java.util.List;

import static duke.level.Level.TILE_SIZE;

public class SecurityCamera extends Active implements Movable, Updatable, SpriteRenderable {
    public SecurityCamera(int x, int y) {
        super(x, y, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public void update() {
    }

    @Override
    public Animation getAnimation() {
        return animation;
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

    private static Animation animation = new Animation(DESCRIPTORS.getFirst());

}

package duke.gameplay.effects;

import duke.gameplay.GameplayContext;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.resources.AssetManager;

public class Particle extends Effect {
    protected Particle(int x, int y, int velocityX, int velocityY, Particle.Type type) {
        super(x, y, createDescriptor(1 + type.ordinal()), TTL);

        setVelocityX(velocityX);
        setVelocityY(velocityY);
    }

    @Override
    public void update(GameplayContext context) {
        super.update(context);

        setVelocityY(getVelocityY() + 1);
    }

    private static AnimationDescriptor createDescriptor(int index) {
        SpriteDescriptor descriptor = new SpriteDescriptor(AssetManager::getObjects, index, 0, 0, 1, 1);

        return new AnimationDescriptor(descriptor, 1, 1);
    }

    public enum Type {
        PINK,
        BLUE,
        WHITE,
        GREEN
    }

    private static final int TTL = 16;
}

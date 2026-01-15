package duke.gameplay.effects;

import duke.gameplay.GameplayContext;
import duke.gfx.SpriteDescriptor;

import static duke.gfx.SpriteDescriptor.OBJECTS;

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

    private static SpriteDescriptor createDescriptor(int index) {
        return new SpriteDescriptor(OBJECTS, index);
    }

    public enum Type {
        PINK,
        BLUE,
        WHITE,
        GREEN
    }

    private static final int TTL = 16;
}

package duke.gameplay.effects;

import duke.gameplay.GameplayContext;
import duke.gfx.SpriteDescriptor;

import java.util.Map;

import static duke.gfx.SpriteDescriptor.OBJECTS;

public class Particle extends Effect {
    protected Particle(int x, int y, int velocityX, int velocityY, Particle.Type type) {
        super(x, y, DESCRIPTORS.get(type), TTL);

        setVelocityX(velocityX);
        setVelocityY(velocityY);
    }

    @Override
    public void update(GameplayContext context) {
        super.update(context);

        setVelocityY(getVelocityY() + 1);
    }


    public enum Type {
        PINK,
        BLUE,
        WHITE,
        GREEN
    }

    private static final int TTL = 16;

    private static SpriteDescriptor createDescriptor(int index) {
        return new SpriteDescriptor(OBJECTS, index);
    }

    static final Map<Particle.Type, SpriteDescriptor> DESCRIPTORS = Map.of(
            Particle.Type.PINK, createDescriptor(1),
            Particle.Type.BLUE, createDescriptor(2),
            Particle.Type.WHITE, createDescriptor(3),
            Particle.Type.GREEN, createDescriptor(4));
}

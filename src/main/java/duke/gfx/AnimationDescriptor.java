package duke.gfx;

public record AnimationDescriptor(SpriteDescriptor spriteDescriptor, int frames, int ticksPerFrame, Type type) {
    public AnimationDescriptor {
        if (frames < 1) {
            throw new IllegalArgumentException("at least one frame is required");
        }

        if (ticksPerFrame < 1) {
            throw new IllegalArgumentException("ticksPerFrame must be at least 1");
        }
    }

    public AnimationDescriptor(SpriteDescriptor spriteDescriptor, int frames, int ticksPerFrame) {
        this(spriteDescriptor, frames, ticksPerFrame, Type.LOOP);
    }

    public enum Type {
        LOOP,
        ONE_SHOT
    }
}

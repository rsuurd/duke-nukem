package duke.gfx;

public record AnimationDescriptor(SpriteDescriptor spriteDescriptor, int frames, int ticksPerFrame) {
    public AnimationDescriptor {
        if (frames < 1) {
            throw new IllegalArgumentException("at least one frame is required");
        }

        if (ticksPerFrame < 1) {
            throw new IllegalArgumentException("ticksPerFrame must be at least 1");
        }
    }
}

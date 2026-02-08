package duke.gfx;

import java.util.ArrayList;
import java.util.List;

public class AnimationDescriptor {
    private List<SpriteDescriptor> descriptors;
    private int ticksPerFrame;
    private Type type;

    public AnimationDescriptor(SpriteDescriptor spriteDescriptor, int frames, int ticksPerFrame) {
        this(spriteDescriptor, frames, ticksPerFrame, Type.LOOP);
    }

    public AnimationDescriptor(SpriteDescriptor spriteDescriptor, int frames, int ticksPerFrame, Type type) {
        this(createDescriptors(spriteDescriptor, frames), ticksPerFrame, type);
    }

    public AnimationDescriptor(List<SpriteDescriptor> frameDescriptors, int ticksPerFrame, Type type) {
        if (frameDescriptors.isEmpty()) {
            throw new IllegalArgumentException("at least one frame is required");
        }

        if (ticksPerFrame < 1) {
            throw new IllegalArgumentException("ticksPerFrame must be at least 1");
        }

        this.descriptors = frameDescriptors;
        this.ticksPerFrame = ticksPerFrame;
        this.type = type;
    }

    private static List<SpriteDescriptor> createDescriptors(SpriteDescriptor descriptor, int frames) {
        List<SpriteDescriptor> descriptors = new ArrayList<>(frames);

        for (int i = 0; i < frames; i++) {
            descriptors.add(descriptor.withBaseIndex(descriptor.baseIndex() + i * (descriptor.rows() * descriptor.cols())));
        }

        return descriptors;
    }

    public List<SpriteDescriptor> getDescriptors() {
        return descriptors;
    }

    public int getFrames() {
        return descriptors.size();
    }

    public int getTicksPerFrame() {
        return ticksPerFrame;
    }

    public Type getType() {
        return type;
    }

    public AnimationDescriptor withTicksPerFrame(int ticksPerFrame) {
        return new AnimationDescriptor(descriptors, ticksPerFrame, type);
    }

    public enum Type {
        LOOP,
        ONE_SHOT
    }
}

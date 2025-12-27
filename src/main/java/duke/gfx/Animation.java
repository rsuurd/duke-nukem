package duke.gfx;

public class Animation {
    private AnimationDescriptor descriptor;

    private int currentFrame;
    private int timer;

    public Animation(AnimationDescriptor descriptor) {
        setAnimation(descriptor);
    }

    public void setAnimation(AnimationDescriptor descriptor) {
        if (this.descriptor != descriptor) {
            this.descriptor = descriptor;
            this.currentFrame = 0;
            this.timer = 0;
        }
    }

    public SpriteDescriptor getSpriteDescriptor() {
        return descriptor.spriteDescriptor();
    }

    public void tick() {
        if (descriptor.frames() <= 1 || descriptor.ticksPerFrame() <= 0) return;

        if (++timer >= descriptor.ticksPerFrame()) {
            timer = 0;
            currentFrame = (currentFrame + 1) % descriptor.frames();
        }
    }

    public int getCurrentBaseIndex() {
        SpriteDescriptor base = descriptor.spriteDescriptor();

        return base.baseIndex() + (currentFrame * (base.rows() * base.cols()));
    }
}

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
            // ensure you call tick() AFTER setting animation
            this.timer = -1;
        }
    }

    public SpriteDescriptor getSpriteDescriptor() {
        return descriptor.spriteDescriptor();
    }

    public void tick() {
        if (isFinished() || descriptor.frames() <= 1 || descriptor.ticksPerFrame() <= 0) return;

        if (++timer >= descriptor.ticksPerFrame()) {
            advanceFrame();
        }
    }

    private void advanceFrame() {
        if (descriptor.type() == AnimationDescriptor.Type.LOOP) {
            currentFrame = (currentFrame + 1) % descriptor.frames();
            timer = 0;
        } else if (currentFrame < lastFrame()) {
            currentFrame++;
            timer = 0;
        }
    }

    private int lastFrame() {
        return descriptor.frames() - 1;
    }

    public int getCurrentBaseIndex() {
        SpriteDescriptor base = descriptor.spriteDescriptor();

        return base.baseIndex() + (currentFrame * (base.rows() * base.cols()));
    }

    public boolean isFinished() {
        return descriptor.type() == AnimationDescriptor.Type.ONE_SHOT && currentFrame == lastFrame() && timer == descriptor.ticksPerFrame();
    }
}

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
            reset();
        }
    }

    public SpriteDescriptor getSpriteDescriptor() {
        return descriptor.getDescriptors().get(currentFrame);
    }

    public void tick() {
        if (isFinished() || descriptor.getFrames() <= 1 || descriptor.getTicksPerFrame() <= 0) return;

        if (++timer >= descriptor.getTicksPerFrame()) {
            advanceFrame();
        }
    }

    private void advanceFrame() {
        if (descriptor.getType() == AnimationDescriptor.Type.LOOP) {
            currentFrame = (currentFrame + 1) % descriptor.getFrames();
            timer = 0;
        } else if (currentFrame < lastFrame()) {
            currentFrame++;
            timer = 0;
        }
    }

    private int lastFrame() {
        return descriptor.getFrames() - 1;
    }

    public void reset() {
        currentFrame = 0;
        timer = 0;
    }

    public boolean isFinished() {
        return descriptor.getType() == AnimationDescriptor.Type.ONE_SHOT && currentFrame >= lastFrame() && timer == descriptor.getTicksPerFrame();
    }
}

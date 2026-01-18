package duke.gfx;

public class Animation {
    private AnimationDescriptor descriptor;

    private int currentFrame;
    private int timer;

    private Direction direction;

    public Animation(AnimationDescriptor descriptor) {
        direction = Direction.FORWARD;

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
            currentFrame = (currentFrame + direction.step() + descriptor.getFrames()) % descriptor.getFrames();
            timer = 0;
        } else if (currentFrame < lastFrame()) {
            currentFrame += direction.step();
            timer = 0;
        }
    }

    private int lastFrame() {
        return (direction == Direction.FORWARD) ? descriptor.getFrames() - 1 : 0;
    }

    public void reset() {
        timer = 0;
        currentFrame = (direction == Direction.FORWARD) ? 0 : descriptor.getFrames() - 1;
    }

    public boolean isFinished() {
        if (descriptor.getType() != AnimationDescriptor.Type.ONE_SHOT) return false;

        return currentFrame == lastFrame();
    }

    public void reverse() {
        direction = direction.reverse();
    }

    private enum Direction {
        FORWARD,
        BACKWARD;

        private Direction reverse() {
            return this == FORWARD ? BACKWARD : FORWARD;
        }

        private int step() {
            return this == FORWARD ? 1 : -1;
        }
    }
}

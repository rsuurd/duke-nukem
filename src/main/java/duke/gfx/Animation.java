package duke.gfx;

public class Animation {
    private Direction direction;
    private AnimationDescriptor descriptor;
    private int totalTicks;

    private int ticks;

    public Animation(AnimationDescriptor descriptor) {
        direction = Direction.FORWARD;

        setAnimation(descriptor);
    }

    public void setAnimation(AnimationDescriptor descriptor) {
        setAnimation(descriptor, true);
    }

    public void setAnimation(AnimationDescriptor descriptor, boolean reset) {
        if (this.descriptor != descriptor) {
            this.descriptor = descriptor;
            this.totalTicks = descriptor.getFrames() * descriptor.getTicksPerFrame();

            if (reset) {
                reset();
            }
        }
    }

    public SpriteDescriptor getSpriteDescriptor() {
        return descriptor.getDescriptors().get(getCurrentFrameIndex());
    }

    int getCurrentFrameIndex() {
        return ticks / descriptor.getTicksPerFrame();
    }

    public void tick() {
        if (isFinished()) return;

        this.ticks = ticks + direction.step();

        if (descriptor.getType() == AnimationDescriptor.Type.ONE_SHOT) {
            ticks = Math.max(0, Math.min(ticks, totalTicks - 1));
        } else {
            ticks = (ticks + totalTicks) % totalTicks;
        }
    }

    public void reset() {
        if (direction == Direction.FORWARD) {
            ticks = 0;
        } else {
            ticks = totalTicks - 1;
        }
    }

    public boolean isFinished() {
        if (descriptor.getType() != AnimationDescriptor.Type.ONE_SHOT) return false;

        if (direction == Direction.FORWARD) {
            return ticks >= totalTicks - 1;
        } else {
            return ticks <= 0;
        }
    }

    public boolean isReset() {
        if (direction == Direction.FORWARD) {
            return ticks == 0;
        } else {
            return ticks == totalTicks - 1;
        }
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

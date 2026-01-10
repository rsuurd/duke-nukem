package duke.gfx;

import duke.gameplay.Layer;

public class AnimatedSpriteRenderable implements SpriteRenderable {
    private Animation animation;
    private Layer layer;

    public AnimatedSpriteRenderable(AnimationDescriptor animationDescriptor) {
        this(animationDescriptor, Layer.FOREGROUND);
    }

    public AnimatedSpriteRenderable(AnimationDescriptor animationDescriptor, Layer layer) {
        this(new Animation(animationDescriptor), layer);
    }

    AnimatedSpriteRenderable(Animation animation, Layer layer) {
        this.animation = animation;
        this.layer = layer;
    }

    public void setAnimation(AnimationDescriptor descriptor) {
        animation.setAnimation(descriptor);
    }

    public void tick() {
        animation.tick();
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    @Override
    public Layer getLayer() {
        return layer;
    }
}

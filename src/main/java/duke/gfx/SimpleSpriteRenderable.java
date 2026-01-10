package duke.gfx;

import duke.gameplay.Layer;

public record SimpleSpriteRenderable(SpriteDescriptor spriteDescriptor, Layer layer) implements SpriteRenderable {
    public SimpleSpriteRenderable(SpriteDescriptor spriteDescriptor) {
        this(spriteDescriptor, Layer.FOREGROUND);
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return spriteDescriptor;
    }

    @Override
    public Layer getLayer() {
        return layer;
    }
}

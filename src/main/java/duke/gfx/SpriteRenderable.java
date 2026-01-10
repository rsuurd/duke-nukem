package duke.gfx;

import duke.gameplay.Layer;

public interface SpriteRenderable {
    SpriteDescriptor getSpriteDescriptor();

    default Layer getLayer() {
        return Layer.FOREGROUND;
    }
}

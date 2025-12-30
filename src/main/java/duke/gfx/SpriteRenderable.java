package duke.gfx;

import duke.gameplay.Layer;

public interface SpriteRenderable {
    int getX();

    int getY();

    SpriteDescriptor getSpriteDescriptor();

    default Layer getLayer() {
        return Layer.FOREGROUND;
    }
}

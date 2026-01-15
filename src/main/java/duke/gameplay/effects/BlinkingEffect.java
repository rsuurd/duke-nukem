package duke.gameplay.effects;

import duke.gfx.SpriteDescriptor;

public class BlinkingEffect extends Effect {
    public BlinkingEffect(int x, int y, SpriteDescriptor spriteDescriptor, int ttl) {
        super(x, y, spriteDescriptor, ttl);
    }

    @Override
    public boolean isVisible() {
        return ttl % 2 == 0;
    }
}

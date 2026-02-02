package duke.gameplay.active;

import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Solid;
import duke.gameplay.Updatable;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.sfx.Sfx;

public class ForceField extends Active implements SpriteRenderable, Updatable, Solid {
    private Animation animation;
    private int sound;

    public ForceField(int x, int y) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        this.animation = new Animation(DESCRIPTOR);

        sound = 0;
    }

    @Override
    public void update(GameplayContext context) {
        animation.tick();

        if (sound == 0) {
            context.getSoundManager().play(Sfx.FORCE_FIELD);
        }

        sound = (sound + 1) % 4; // TODO probably tied to animation frame rate (if animation.getFrame() == 0 play sound)
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    private static final AnimationDescriptor DESCRIPTOR = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.OBJECTS, 65), 4, 1);
}

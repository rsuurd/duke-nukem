package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Solid;
import duke.gameplay.Updatable;
import duke.gfx.*;
import duke.sfx.Sfx;

import static duke.level.Level.TILE_SIZE;

public class ForceField extends Active implements Renderable, Updatable, Solid {
    private Animation animation;
    private int sound;

    public ForceField(int x, int y, int height) {
        super(x, y, TILE_SIZE, height);

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
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        for (int offsetY = 0; offsetY < getHeight(); offsetY += TILE_SIZE) {
            spriteRenderer.render(renderer, animation.getSpriteDescriptor(), screenX, screenY + offsetY);
        }
    }

    private static final AnimationDescriptor DESCRIPTOR = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.OBJECTS, 65), 4, 1);
}

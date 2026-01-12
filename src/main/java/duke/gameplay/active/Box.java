package duke.gameplay.active;

import duke.gameplay.*;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.sfx.Sfx;

import java.util.function.BiFunction;

public class Box extends Active implements SpriteRenderable, Physics, Shootable {
    private SpriteDescriptor spriteDescriptor;
    private BiFunction<Integer, Integer, ? extends Active> contents;

    public Box(int x, int y, SpriteDescriptor spriteDescriptor, BiFunction<Integer, Integer, ? extends Active> contents) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        this.spriteDescriptor = spriteDescriptor;
        this.contents = contents;
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        spawnContents(context);

        context.getActiveManager().spawn(EffectsFactory.createParticles(getX(), getY()));
        context.getSoundManager().play(Sfx.BOX_EXPLODE);

        destroy();
    }

    private void spawnContents(GameplayContext context) {
        if (contents == null) return;

        Active active = contents.apply(getX(), getY());

        if (active != null) {
            context.getActiveManager().spawn(active);
        }
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return spriteDescriptor;
    }
}

package duke.gameplay.effects;

import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.SpriteRenderable;
import duke.gameplay.Updatable;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.level.Level;
import duke.resources.AssetManager;

public class Sparks extends Active implements Updatable, SpriteRenderable {
    private Animation animation;

    public Sparks(int x, int y) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        animation = new Animation(new AnimationDescriptor(new SpriteDescriptor(AssetManager::getAnim, 42, 0, 0, 1, 1), 6, 1, AnimationDescriptor.Type.ONE_SHOT));
    }

    @Override
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public void update(GameplayContext context) {
        animation.tick();
    }
}

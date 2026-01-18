package duke.gameplay.active.items;

import duke.gameplay.Collidable;
import duke.gameplay.GameplayContext;
import duke.gameplay.active.items.behavior.ItemBehavior;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.SpriteRenderable;
import duke.level.Level;

public class FizzingSoda extends Item implements Collidable {
    private boolean bumped;

    public FizzingSoda(int x, int y, SpriteRenderable renderable, ItemBehavior behavior) {
        super(x, y, renderable, behavior);

        setVelocityY(-Level.HALF_TILE_SIZE);
    }

    @Override
    public void onCollision(Direction direction) {
        bumped = direction == Direction.UP;
    }

    @Override
    public void update(GameplayContext context) {
        super.update(context);

        if (bumped) {
            destroy();
            context.getActiveManager().spawn(EffectsFactory.createSparks(getX(), getY()));
        }
    }

    @Override
    public int getVerticalAcceleration() {
        return 0;
    }
}

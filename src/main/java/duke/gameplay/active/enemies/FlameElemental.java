package duke.gameplay.active.enemies;

import duke.gameplay.Bolt;
import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.Health;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.active.enemies.behavior.PatrolBehavior;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;

import static duke.gfx.SpriteDescriptor.ANIM;
import static duke.level.Level.TILE_SIZE;

public class FlameElemental extends Enemy implements SpriteRenderable {
    private Animation animation;

    public FlameElemental(int x, int y, Facing facing) {
        this(x, y, facing, new PatrolBehavior(2, 4), null);
    }

    FlameElemental(int x, int y, Facing facing, EnemyBehavior behavior, Health health) {
        super(x, y, TILE_SIZE, TILE_SIZE, facing, behavior, health);

        animation = new Animation((facing == Facing.LEFT) ? ANIM_LEFT : ANIM_RIGHT);
    }

    @Override
    public void update(GameplayContext context) {
        super.update(context);

        animation.tick();
    }

    @Override
    protected void onFacingChanged(Facing facing) {
        animation.setAnimation((facing == Facing.LEFT) ? ANIM_LEFT : ANIM_RIGHT);
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
    }

    @Override
    protected void onDestroyed(GameplayContext context) {
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    private static final SpriteDescriptor LEFT = new SpriteDescriptor(ANIM, 250, 0, -TILE_SIZE, 2, 1);
    private static final SpriteDescriptor RIGHT = new SpriteDescriptor(ANIM, 256, 0, -TILE_SIZE, 2, 1);
    private static final AnimationDescriptor ANIM_LEFT = new AnimationDescriptor(LEFT, 3, 4);
    private static final AnimationDescriptor ANIM_RIGHT = new AnimationDescriptor(RIGHT, 3, 4);
}

package duke.gameplay.active.enemies;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.Physics;
import duke.gameplay.Shootable;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.active.enemies.behavior.PatrolBehavior;
import duke.gameplay.effects.TechbotDestruction;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;

import static duke.level.Level.HALF_TILE_SIZE;
import static duke.level.Level.TILE_SIZE;

public class Techbot extends Enemy implements Physics, Shootable, SpriteRenderable {
    private Animation animation;

    public Techbot(int x, int y) {
        this(x, y, new PatrolBehavior(BEHAVE_INTERVAL, HALF_TILE_SIZE));
    }

    Techbot(int x, int y, EnemyBehavior behavior) {
        super(x, y, TILE_SIZE, TILE_SIZE, Facing.RIGHT, behavior);

        animation = new Animation(DESCRIPTOR);
    }

    @Override
    public void update(GameplayContext context) {
        super.update(context);

        animation.tick();
    }

    @Override
    protected void onDestroyed(GameplayContext context) {
        context.getScoreManager().score(100);
        context.getActiveManager().spawn(new TechbotDestruction(getX(), getY()));
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    private static AnimationDescriptor DESCRIPTOR = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.ANIM, 82, 0, 0, 1, 1), 3, 1);
    private static final int BEHAVE_INTERVAL = 3;
}

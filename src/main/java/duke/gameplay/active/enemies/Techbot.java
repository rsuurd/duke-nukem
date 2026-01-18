package duke.gameplay.active.enemies;

import duke.gameplay.*;
import duke.gameplay.effects.TechbotDestruction;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;

import static duke.level.Level.HALF_TILE_SIZE;
import static duke.level.Level.TILE_SIZE;

public class Techbot extends Active implements Updatable, Physics, Damaging, Shootable, SpriteRenderable {
    private Animation animation;
    private EnemyBehavior behavior;

    public Techbot(int x, int y) {
        this(x, y, new PatrolBehavior(Facing.RIGHT, BEHAVE_INTERVAL, HALF_TILE_SIZE));
    }

    Techbot(int x, int y, EnemyBehavior behavior) {
        super(x, y, TILE_SIZE, TILE_SIZE);

        animation = new Animation(DESCRIPTOR);
        this.behavior = behavior;
    }

    @Override
    public void update(GameplayContext context) {
        behavior.behave(context, this);
        animation.tick();
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        context.getActiveManager().spawn(new TechbotDestruction(getX(), getY()));

        destroy();
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    private static AnimationDescriptor DESCRIPTOR = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.ANIM, 82, 0, 0, 1, 1), 3, 1);
    private static final int BEHAVE_INTERVAL = 3;
}

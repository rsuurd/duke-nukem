package duke.gameplay.active.enemies;

import duke.gameplay.BonusTracker;
import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.active.Wakeable;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.active.enemies.behavior.SnakeBotBehavior;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.sfx.Sfx;

import static duke.gfx.SpriteDescriptor.ANIM;
import static duke.level.Level.TILE_SIZE;

public class SnakeBot extends Enemy implements SpriteRenderable, Wakeable {
    private Animation animation;

    public SnakeBot(int x, int y, int index) {
        this(x, y, new SnakeBotBehavior(index * 2));
    }

    SnakeBot(int x, int y, EnemyBehavior behavior) {
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
        context.getActiveManager().spawn(EffectsFactory.createFlash(getX(), getY()));
        context.getActiveManager().spawn(EffectsFactory.createParticles(getX(), getY()));
        context.getScoreManager().score(1000);
        context.getSoundManager().play(Sfx.BOX_EXPLODE);
        context.getBonusTracker().trackDestroyed(BonusTracker.Type.SNAKE);
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    public static final int SIZE = 8;
    private static final AnimationDescriptor DESCRIPTOR = new AnimationDescriptor(new SpriteDescriptor(ANIM, 124), 8, 1);
}

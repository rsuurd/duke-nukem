package duke.gameplay.active.enemies;

import duke.gameplay.*;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.active.enemies.behavior.WallCrawlBehavior;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.sfx.Sfx;

import static duke.level.Level.TILE_SIZE;

public class WallCrawler extends Active implements Updatable, Damaging, Shootable, SpriteRenderable {
    private Animation animation;
    private EnemyBehavior behavior;

    public WallCrawler(int x, int y, Facing facing) {
        this(x, y, facing, new WallCrawlBehavior(facing, BEHAVE_INTERVAL, 1));
    }

    WallCrawler(int x, int y, Facing facing, EnemyBehavior behavior) {
        super(x, y, TILE_SIZE, TILE_SIZE);

        SpriteDescriptor spriteDescriptor = (facing == Facing.RIGHT) ? RIGHT_DESCRIPTOR : LEFT_DESCRIPTOR;
        AnimationDescriptor animationDescriptor = new AnimationDescriptor(spriteDescriptor, 4, 2);
        animation = new Animation(animationDescriptor);
        this.behavior = behavior;
    }

    @Override
    public void update(GameplayContext context) {
        behavior.behave(context, this);

        animation.tick();
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        context.getScoreManager().score(100);
        context.getActiveManager().spawn(EffectsFactory.createSmoke(getX(), getY()));
        context.getSoundManager().play(Sfx.SMALL_DEATH);

        destroy();
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    public void reverse() {
        animation.reverse();
    }

    private static final int BEHAVE_INTERVAL = 1;
    private static final SpriteDescriptor RIGHT_DESCRIPTOR = new SpriteDescriptor(SpriteDescriptor.ANIM, 140, 0, 0, 1, 1);
    private static final SpriteDescriptor LEFT_DESCRIPTOR = RIGHT_DESCRIPTOR.withBaseIndex(144);
}

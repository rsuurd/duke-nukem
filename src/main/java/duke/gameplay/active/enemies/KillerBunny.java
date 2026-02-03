package duke.gameplay.active.enemies;

import duke.gameplay.*;
import duke.gameplay.active.Wakeable;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.active.enemies.behavior.PatrolBehavior;
import duke.gameplay.effects.EffectsFactory;
import duke.gameplay.effects.KillerBunnySpin;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.sfx.Sfx;

import static duke.gfx.SpriteDescriptor.ANIM;
import static duke.level.Level.TILE_SIZE;

public class KillerBunny extends Enemy implements Physics, SpriteRenderable, Wakeable {
    private Animation animation;
    private boolean turning;

    public KillerBunny(int x, int y) {
        // TODO verify if 16x16 is correct bounding box, otherwise we need to adjust patrol behavior to accept taller enemies
        this(x, y, Facing.LEFT, new PatrolBehavior(1, SPEED));
    }

    protected KillerBunny(int x, int y, Facing facing, EnemyBehavior behavior) {
        super(x, y, TILE_SIZE, TILE_SIZE, facing, behavior);

        animation = new Animation(LEFT);
    }

    @Override
    public void update(GameplayContext context) {
        if (turning) { // short pause while turning around
            turning = false;
        } else {
            super.update(context);

            animation.tick();
        }

        if (context.getPlayer().intersects(this)) {
            onPlayerCollision(context);
        }
    }

    private void onPlayerCollision(GameplayContext context) {
        destroy();
        onDestroyed(context);
        context.getPlayer().getHealth().takeDamage(1);
        context.getSoundManager().play(Sfx.RABBIT_GONE);
        context.getActiveManager().spawn(new KillerBunnySpin(getX(), getY()));
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        super.onShot(context, bolt);

        context.getSoundManager().play(Sfx.HIT_A_BREAKER); // verify
        context.getScoreManager().score(200);
        context.getActiveManager().spawn(EffectsFactory.createKillerBunnySmoke(getX(), getY() - TILE_SIZE));
    }

    @Override
    protected void onDestroyed(GameplayContext context) {
        context.getBonusTracker().trackDestroyed(BonusTracker.Type.BUNNY);
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return turning ? FACE_FRONT : animation.getSpriteDescriptor();
    }

    @Override
    protected void onFacingChanged(Facing facing) {
        turning = true;

        animation.setAnimation(facing == Facing.LEFT ? LEFT : RIGHT);
    }

    @Override
    public boolean isAbleToMove() {
        return !turning;
    }

    static final int SPEED = 4;

    private AnimationDescriptor LEFT = new AnimationDescriptor(new SpriteDescriptor(ANIM, 118, 0, -TILE_SIZE, 2, 1), 3, 1);
    private AnimationDescriptor RIGHT = new AnimationDescriptor(new SpriteDescriptor(ANIM, 236, 0, -TILE_SIZE, 2, 1), 3, 1);
    private SpriteDescriptor FACE_FRONT = new SpriteDescriptor(ANIM, 244, 0, -TILE_SIZE, 2, 1);
}

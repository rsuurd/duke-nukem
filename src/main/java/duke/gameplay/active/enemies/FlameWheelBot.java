package duke.gameplay.active.enemies;

import duke.gameplay.*;
import duke.gameplay.active.Wakeable;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.active.enemies.behavior.PatrolBehavior;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.sfx.Sfx;

import static duke.level.Level.HALF_TILE_SIZE;
import static duke.level.Level.TILE_SIZE;

public class FlameWheelBot extends Enemy implements Wakeable, SpriteRenderable {
    private boolean patrolling;
    private boolean flameOn;
    private Animation animation;
    private int timer;
    private int smoking;

    public FlameWheelBot(int x, int y) {
        this(x, y, Facing.LEFT, new PatrolBehavior(2, 8), new Health(HEALTH));
    }

    FlameWheelBot(int x, int y, Facing facing, EnemyBehavior behavior, Health health) {
        super(x, y, TILE_SIZE, TILE_SIZE, facing, behavior, health);

        patrolling = false;
        flameOn = false;
        animation = new Animation(FLAME_OFF);
        timer = 0;
        smoking = 0;
    }

    @Override
    public void update(GameplayContext context) {
        super.update(context);

        if (++timer >= TOGGLE_TIME) {
            toggleFlame();
            timer = 0;
        }

        animation.tick();

        visualizeDamage(context);
    }

    @Override
    public boolean isAwake() {
        return patrolling;
    }

    @Override
    public void wakeUp() {
        patrolling = true;
    }

    @Override
    protected void onDestroyed(GameplayContext context) {
        context.getScoreManager().score(5000);
        context.getSoundManager().play(Sfx.BOX_EXPLODE);

        ActiveManager manager = context.getActiveManager();

        // does this depend on direction traveled?
        manager.spawn(EffectsFactory.createSparks(getX() - HALF_TILE_SIZE, getY() - TILE_SIZE));
        manager.spawn(EffectsFactory.createSparks(getX(), getY() - TILE_SIZE));
        manager.spawn(EffectsFactory.createSparks(getX(), getY()));
        manager.spawn(EffectsFactory.createSmoke(getX() + HALF_TILE_SIZE, getY() - HALF_TILE_SIZE));
        manager.spawn(EffectsFactory.createParticles(getX() - HALF_TILE_SIZE, getY() - 4));
        manager.spawn(EffectsFactory.createParticles(getX() + HALF_TILE_SIZE, getY() - 4));
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        if (!flameOn) {
            super.onShot(context, bolt);
        }
    }

    private void toggleFlame() {
        flameOn = !flameOn;
        animation = new Animation(flameOn ? FLAME_ON : FLAME_OFF);
    }

    private void visualizeDamage(GameplayContext context) {
        if (health.getCurrent() < health.getMax() && ++smoking % 4 == 0) {
            context.getActiveManager().spawn(EffectsFactory.createSmoke(getX(), getY() - HALF_TILE_SIZE));
        }
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    boolean isFlameOn() {
        return flameOn;
    }

    private static final SpriteDescriptor BASE = new SpriteDescriptor(SpriteDescriptor.ANIM, 50, -8, -TILE_SIZE, 2, 2);
    private static final AnimationDescriptor FLAME_OFF = new AnimationDescriptor(BASE, 4, 1);
    private static final AnimationDescriptor FLAME_ON = new AnimationDescriptor(BASE.withBaseIndex(66), 4, 1);

    private static final int HEALTH = 2;
    static final int TOGGLE_TIME = 32;
}

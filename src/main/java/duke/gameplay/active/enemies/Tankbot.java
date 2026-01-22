package duke.gameplay.active.enemies;

import duke.gameplay.*;
import duke.gameplay.active.Wakeable;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.active.enemies.behavior.PatrolBehavior;
import duke.gameplay.effects.EffectsFactory;
import duke.gameplay.player.Player;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.sfx.Sfx;

import java.util.Map;

import static duke.level.Level.HALF_TILE_SIZE;
import static duke.level.Level.TILE_SIZE;

public class Tankbot extends Enemy implements Physics, Shootable, SpriteRenderable, Wakeable {
    private Animation animation;

    private boolean turnedAround;
    protected boolean awake;
    private int smoking;

    public Tankbot(int x, int y) {
        this(x, y, Facing.LEFT, new PatrolBehavior(1, 4), new Health(HEALTH));
    }

    Tankbot(int x, int y, Facing facing, EnemyBehavior behavior, Health health) {
        super(x, y, 2 * TILE_SIZE, TILE_SIZE, facing, behavior, health);
        animation = new Animation(DESCRIPTORS.get(facing));
    }

    @Override
    protected void onFacingChanged(Facing facing) {
        turnedAround = true;
        animation.setAnimation(DESCRIPTORS.get(facing));
    }

    @Override
    public void update(GameplayContext context) {
        turnedAround = false;

        super.update(context);

        if (turnedAround) {
            tryShoot(context);
        }

        animation.tick();

        visualizeDamage(context);
    }

    private void tryShoot(GameplayContext context) {
        if (isPlayerInRange(context.getPlayer())) {
            int bulletX = getX() - HALF_TILE_SIZE + (getFacing() == Facing.RIGHT ? getWidth() : 0);
            context.getActiveManager().spawn(new EnemyFire(bulletX, getY(), getFacing()));
            context.getSoundManager().play(Sfx.ENEMY_SHOT);
        }
    }

    private boolean isPlayerInRange(Player player) {
        int horizontalDistance = Math.abs((getX() + getWidth() / 2) - (player.getX() + player.getWidth() / 2));
        int verticalDistance = Math.abs((getY() + getHeight() / 2) - (player.getY() + player.getHeight() / 2));

        return horizontalDistance < SHOOTING_RANGE && verticalDistance < SHOOTING_RANGE;
    }

    private void visualizeDamage(GameplayContext context) {
        if (health.getCurrent() < health.getMax() && ++smoking % 6 == 0) {
            int smokeX = (getFacing() == Facing.LEFT) ? getX() + TILE_SIZE : getX();
            context.getActiveManager().spawn(EffectsFactory.createSmoke(smokeX, getY() - HALF_TILE_SIZE));
        }
    }

    @Override
    protected void onDestroyed(GameplayContext context) {
        context.getSoundManager().play(Sfx.SMALL_DEATH);
        context.getScoreManager().score(2500);
        context.getActiveManager().spawn(EffectsFactory.createSparks(getX() + HALF_TILE_SIZE, getY()));
        context.getActiveManager().spawn(EffectsFactory.createParticles(getX(), getY()));
        context.getActiveManager().spawn(EffectsFactory.createParticles(getX() + TILE_SIZE, getY()));
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    @Override
    public boolean isAwake() {
        return awake;
    }

    @Override
    public void wakeUp() {
        awake = true;
    }

    private static final int HEALTH = 2;
    private static final int SHOOTING_RANGE = 8 * TILE_SIZE;
    private static final SpriteDescriptor TANKBOT_DESCRIPTOR = new SpriteDescriptor(SpriteDescriptor.ANIM, 34, 0, 0, 1, 2);

    private static final Map<Facing, AnimationDescriptor> DESCRIPTORS = Map.of(
            Facing.LEFT, new AnimationDescriptor(TANKBOT_DESCRIPTOR, 2, 8),
            Facing.RIGHT, new AnimationDescriptor(TANKBOT_DESCRIPTOR.withBaseIndex(38), 2, 8)
    );
}

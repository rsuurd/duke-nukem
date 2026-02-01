package duke.gameplay.active.enemies;

import duke.Renderer;
import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.Physics;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.active.enemies.behavior.FlyingBotBehavior;
import duke.gameplay.effects.EffectsFactory;
import duke.gameplay.effects.FlyingBotCrash;
import duke.gameplay.player.Player;
import duke.gfx.*;
import duke.sfx.Sfx;

import static duke.level.Level.TILE_SIZE;

public class FlyingBot extends Enemy implements Physics, Renderable {
    private SpriteDescriptor current;
    private Animation animation;
    private boolean shooting;

    public FlyingBot(int x, int y) {
        this(x, y, Facing.LEFT, new FlyingBotBehavior());
    }

    FlyingBot(int x, int y, Facing facing, EnemyBehavior behavior) {
        super(x, y, TILE_SIZE, TILE_SIZE, facing, behavior);

        current = TOP_DESCRIPTOR.getDescriptors().getFirst();
        animation = new Animation(BOTTOM_DESCRIPTOR);
    }

    @Override
    public void update(GameplayContext context) {
        super.update(context);

        if (context.getPlayer().intersects(this)) {
            onCollision(context);
        }

        if (shooting) {
            onShoot(context);
            shooting = false;
        }

        animation.tick();
    }

    private void onCollision(GameplayContext context) {
        Player player = context.getPlayer();

        if (!isStompedBy(player)) {
            // As this enemy is destroyed on touch, the Damaging interface won't work properly
            // because it's considered destroyed before and no damage is applied
            // that's why we directly apply damage here for now
            player.getHealth().takeDamage(1);
        }

        destroy();
        onDestroyed(context);
    }

    private boolean isStompedBy(Player player) {
        int previousPlayerY = player.getY() - player.getVelocityY();
        int previousPlayerBottom = previousPlayerY + player.getHeight();

        return previousPlayerBottom <= getY() && player.getVelocityY() > 0;
    }

    @Override
    public void shoot() {
        shooting = true;
    }

    private void onShoot(GameplayContext context) {
        int bulletX = getX() + (getFacing() == Facing.LEFT ? -TILE_SIZE : TILE_SIZE);
        int bulletY = getY() + 2;

        context.getActiveManager().spawn(new EnemyFire(bulletX, bulletY, getFacing()));
        context.getSoundManager().play(Sfx.ENEMY_SHOT);
    }

    @Override
    public int getVerticalAcceleration() {
        return 0;
    }

    @Override
    protected void onDestroyed(GameplayContext context) {
        context.getScoreManager().score(200);
        context.getActiveManager().spawn(EffectsFactory.createSparks(getX(), getY()));
        context.getSoundManager().play(Sfx.SMALL_DEATH);
        crash(context);
    }

    private void crash(GameplayContext context) {
        Player player = context.getPlayer();

        int crashVelocityX = (player.getX() < getX()) ? CRASH_SPEED : -CRASH_SPEED;
        context.getActiveManager().spawn(new FlyingBotCrash(getX(), getY(), getFacing(), crashVelocityX));
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        spriteRenderer.render(renderer, current, screenX, screenY);
        spriteRenderer.render(renderer, animation.getSpriteDescriptor(), screenX, screenY + getHeight());
    }

    static final int CRASH_SPEED = 4;

    public void setRotationFrame(int angle) { // TODO rename
        current = TOP_DESCRIPTOR.getDescriptors().get(angle);
    }

    private static final AnimationDescriptor TOP_DESCRIPTOR = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.ANIM, 0), 6, 1);
    private static final AnimationDescriptor BOTTOM_DESCRIPTOR = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.ANIM, 6), 4, 1);
}

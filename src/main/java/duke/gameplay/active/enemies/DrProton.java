package duke.gameplay.active.enemies;

import duke.Renderer;
import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.Health;
import duke.gameplay.Physics;
import duke.gameplay.active.Wakeable;
import duke.gameplay.active.enemies.behavior.DrProtonBehavior;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gfx.*;
import duke.sfx.Sfx;

import static duke.level.Level.TILE_SIZE;

public class DrProton extends Enemy implements Physics, Renderable, Wakeable {
    private boolean awake;

    private SpriteDescriptor top;
    private Animation animation;

    private boolean shoot;
    private boolean escaping;

    public DrProton(int x, int y) {
        this(x, y, Facing.RIGHT, new DrProtonBehavior(), new Health(HEALTH));
    }

    DrProton(int x, int y, Facing facing, EnemyBehavior behavior, Health health) {
        super(x, y, 2 * TILE_SIZE, 2 * TILE_SIZE, facing, behavior, health);

        top = (facing == Facing.LEFT) ? TOP_LEFT : TOP_RIGHT;
        animation = new Animation((facing == Facing.LEFT) ? THRUSTERS_LEFT : THRUSTERS_RIGHT);
    }

    @Override
    protected void onFacingChanged(Facing facing) {
        top = (facing == Facing.LEFT) ? TOP_LEFT : TOP_RIGHT;
        animation.setAnimation((facing == Facing.LEFT) ? THRUSTERS_LEFT : THRUSTERS_RIGHT);
    }

    @Override
    public void update(GameplayContext context) {
        if (escaping) {
            if (getY() <= 0) {
                context.getLevel().complete();
            }
        } else {
            super.update(context);
        }

        animation.tick();

        if (shoot) {
            shoot(context);
            shoot = false;
        }
    }

    @Override
    public void shoot() {
        shoot = true;
    }

    private void shoot(GameplayContext context) {
        int bulletX = getX() + (getFacing() == Facing.LEFT ? -TILE_SIZE + 1 : getWidth() - 1);
        int bulletY = getY() + 18;

        context.getActiveManager().spawn(new EnemyFire(bulletX, bulletY, getFacing()));
        context.getSoundManager().play(Sfx.ENEMY_SHOT);
    }

    @Override
    protected void onDestroyed(GameplayContext context) {
        // For episode 3 he crashes down. Episodes 1 & 2 he escapes by flying up
        escaping = true;
        setVelocityX(0);
        setVelocityY(-TILE_SIZE);
        context.getSoundManager().play(Sfx.BAD_GUY_GO_UP);
        context.getViewportManager().setTarget(this);
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        spriteRenderer.render(renderer, top, screenX, screenY);
        spriteRenderer.render(renderer, animation.getSpriteDescriptor(), screenX, screenY + TILE_SIZE);
    }

    @Override
    public int getVerticalAcceleration() {
        return 0;
    }

    @Override
    public boolean isAwake() {
        return awake;
    }

    @Override
    public void wakeUp() {
        awake = true;
    }

    static final int HEALTH = 10;

    public static final SpriteDescriptor TOP_LEFT = new SpriteDescriptor(SpriteDescriptor.ANIM, 280, 0, 0, 2, 2);
    public static final SpriteDescriptor TOP_RIGHT = new SpriteDescriptor(SpriteDescriptor.ANIM, 286, 0, 0, 2, 2);
    public static final AnimationDescriptor THRUSTERS_LEFT = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.ANIM, 282, 0, 0, 1, 2), 2, 1);
    public static final AnimationDescriptor THRUSTERS_RIGHT = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.ANIM, 288, 0, 0, 1, 2), 2, 1);
}

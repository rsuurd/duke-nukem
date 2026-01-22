package duke.gameplay.active.enemies;

import duke.Renderer;
import duke.gameplay.*;
import duke.gameplay.active.enemies.behavior.Ed209Behavior;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.Renderable;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderer;
import duke.sfx.Sfx;

import static duke.level.Level.HALF_TILE_SIZE;
import static duke.level.Level.TILE_SIZE;

public class Ed209 extends Enemy implements Renderable, Physics, Collidable {
    private boolean grounded;
    private boolean shooting;

    private int smoking;

    public Ed209(int x, int y) {
        this(x, y, Facing.LEFT, new Ed209Behavior(), new Health(3));
    }

    Ed209(int x, int y, Facing facing, EnemyBehavior behavior, Health health) {
        super(x, y, TILE_SIZE * 2, TILE_SIZE * 2, facing, behavior, health);

        this.grounded = true;
        this.shooting = false;

        this.smoking = 0;
    }

    @Override
    public int getVerticalAcceleration() {
        return GRAVITY;
    }

    @Override
    protected void onDestroyed(GameplayContext context) {
        context.getScoreManager().score(2000);

        ActiveManager manager = context.getActiveManager();
        manager.spawn(EffectsFactory.createDebris(getX(), getY()));
        manager.spawn(EffectsFactory.createParticles(getX(), getY()));
        manager.spawn(EffectsFactory.createParticles(getX() + HALF_TILE_SIZE, getY() - TILE_SIZE));
        manager.spawn(EffectsFactory.createParticles(getX() + TILE_SIZE, getY() + TILE_SIZE));

        manager.spawn(EffectsFactory.createSparks(getX() + HALF_TILE_SIZE, getY()));
        manager.spawn(EffectsFactory.createSparks(getX() + TILE_SIZE, getY()));
        manager.spawn(EffectsFactory.createSparks(getX() + TILE_SIZE, getY() + HALF_TILE_SIZE));
        manager.spawn(EffectsFactory.createSparks(getX() + HALF_TILE_SIZE, getY() + TILE_SIZE));

        context.getSoundManager().play(Sfx.HIT_A_BREAKER);
    }

    private void visualizeDamage(GameplayContext context) {
        int damageTaken = health.getMax() - health.getCurrent();

        if (damageTaken > 0 && (++smoking % 4) == 0) {
            int smokeX = (getFacing() == Facing.LEFT) ? getX() : getX() + TILE_SIZE;
            context.getActiveManager().spawn(EffectsFactory.createSmoke(smokeX, getY() - 4));

            if (damageTaken >= 2) {
                int secondSmokeX = (getFacing() == Facing.RIGHT) ? getX() : getX() + TILE_SIZE;
                context.getActiveManager().spawn(EffectsFactory.createSmoke(secondSmokeX, getY() - 4));
            }
        }
    }

    @Override
    public void jump() {
        setVelocityY(JUMP_STRENGTH);
        setVelocityX(getFacing() == Facing.LEFT ? -SPEED : SPEED);
        grounded = false;
    }

    @Override
    public void shoot() {
        this.shooting = true;
    }

    @Override
    public void onCollision(Direction direction) {
        grounded = direction == Direction.DOWN;

        if (grounded) {
            setVelocityX(0);
            setVelocityY(0);
        }
    }

    @Override
    public boolean isGrounded() {
        return grounded;
    }

    @Override
    public void update(GameplayContext context) {
        shooting = false;

        super.update(context);

        if (shooting) {
            shoot(context);
        }

        visualizeDamage(context);
    }

    private void shoot(GameplayContext context) {
        int bulletX = getX() + (getFacing() == Facing.LEFT ? -11 : getWidth() - 4);
        int bulletY = getY() + 14;

        context.getActiveManager().spawn(new EnemyFire(bulletX, bulletY, getFacing()));
        context.getSoundManager().play(Sfx.ENEMY_SHOT);
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        boolean facingLeft = getFacing() == Facing.LEFT;

        if (isGrounded()) {
            spriteRenderer.render(renderer, facingLeft ? HEAD_LEFT : HEAD_RIGHT, screenX, screenY);

            if (shooting) {
                spriteRenderer.render(renderer, facingLeft ? SHOOTING_LEFT : SHOOTING_RIGHT, screenX, screenY + TILE_SIZE);
            } else {
                spriteRenderer.render(renderer, facingLeft ? LEGS_LEFT : LEGS_RIGHT, screenX, screenY + TILE_SIZE);
            }
        } else {
            spriteRenderer.render(renderer, facingLeft ? JUMPING_LEFT : JUMPING_RIGHT, screenX, screenY);
        }
    }

    static final int JUMP_STRENGTH = -18;
    static final int SPEED = 4;

    private static final SpriteDescriptor HEAD_LEFT = new SpriteDescriptor(SpriteDescriptor.ANIM, 10, 0, 0, 1, 2);
    private static final SpriteDescriptor LEGS_LEFT = new SpriteDescriptor(SpriteDescriptor.ANIM, 12, 0, 0, 1, 2);
    private static final SpriteDescriptor SHOOTING_LEFT = new SpriteDescriptor(SpriteDescriptor.ANIM, 14, 0, 0, 1, 2);

    private static final SpriteDescriptor HEAD_RIGHT = new SpriteDescriptor(SpriteDescriptor.ANIM, 22, 0, 0, 1, 2);
    private static final SpriteDescriptor LEGS_RIGHT = new SpriteDescriptor(SpriteDescriptor.ANIM, 24, 0, 0, 1, 2);
    private static final SpriteDescriptor SHOOTING_RIGHT = new SpriteDescriptor(SpriteDescriptor.ANIM, 26, 0, 0, 1, 2);

    private static final SpriteDescriptor JUMPING_LEFT = new SpriteDescriptor(SpriteDescriptor.ANIM, 16, 0, -TILE_SIZE, 3, 2);
    private static final SpriteDescriptor JUMPING_RIGHT = new SpriteDescriptor(SpriteDescriptor.ANIM, 28, 0, -TILE_SIZE, 3, 2);
}

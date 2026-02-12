package duke.gameplay.active.enemies;

import duke.Renderer;
import duke.gameplay.*;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.active.enemies.behavior.HelicopterBehavior;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.*;
import duke.sfx.Sfx;

import static duke.level.Level.HALF_TILE_SIZE;
import static duke.level.Level.TILE_SIZE;

public class Helicopter extends Enemy implements Renderable, Physics, Collidable {
    private Animation animation;
    private SpriteDescriptor descriptor;
    private int smoking;
    private boolean crashed;

    public Helicopter(int x, int y) {
        this(x, y, new HelicopterBehavior(), new Health(4));
    }

    Helicopter(int x, int y, EnemyBehavior behavior, Health health) {
        super(x, y, 4 * TILE_SIZE, 2 * TILE_SIZE, Facing.LEFT, behavior, health);

        animation = new Animation(ROTOR_ANIMATION_MID);
        descriptor = FUSELAGE_MID;

        smoking = 0;
        crashed = false;
    }

    @Override
    public void update(GameplayContext context) {
        if (crashed) {
            destroy();
            onDestroyed(context);
        } else {
            super.update(context);

            updateAnimation();
            context.getSoundManager().play(Sfx.WALKING);

            visualizeDamage(context);
        }
    }

    private void updateAnimation() {
        if (getVelocityX() == 0) {
            animation.setAnimation(ROTOR_ANIMATION_MID);
            descriptor = FUSELAGE_MID;
        } else if (getVelocityX() < 0) {
            animation.setAnimation(ROTOR_ANIMATION_LEFT);
            descriptor = FUSELAGE;
        } else if (getVelocityX() > 0) {
            animation.setAnimation(ROTOR_ANIMATION_RIGHT);
            descriptor = FUSELAGE_RIGHT;
        }

        animation.tick();
    }

    private void visualizeDamage(GameplayContext context) {
        int damageTaken = health.getMax() - health.getCurrent();

        if (damageTaken > 0 && (++smoking % 2) == 0) {
            int smokeX = getX() + (getWidth() / 2);
            context.getActiveManager().spawn(EffectsFactory.createSmoke(smokeX, getY()));

            if (damageTaken >= 2) {
                context.getActiveManager().spawn(EffectsFactory.createSmoke(getX() + TILE_SIZE, getY() - HALF_TILE_SIZE));
            }
        }
    }

    @Override
    public int getVerticalAcceleration() {
        return 0;
    }

    @Override
    protected void destroy() {
        if (!crashed) return;

        super.destroy();
    }

    @Override
    protected void onDestroyed(GameplayContext context) {
        if (!crashed) return;

        ActiveManager activeManager = context.getActiveManager();
        activeManager.spawn(EffectsFactory.createDebris(getX(), getY() + TILE_SIZE));
        activeManager.spawn(EffectsFactory.createDebris(getX() + TILE_SIZE, getY()));

        int particleX = getX();
        int particleYOffset = -TILE_SIZE;
        for (int i = 0; i < 5; i++) {
            activeManager.spawn(EffectsFactory.createParticles(particleX, getY() + particleYOffset));
            particleX += 13;
            particleYOffset = -particleYOffset;
        }

        // sparks / smoke

        context.getSoundManager().play(Sfx.BOX_EXPLODE);
        context.getScoreManager().score(5000);
    }

    @Override
    public void onCollision(Direction direction, int flags) {
        if (getHealth().isDead() && direction == Direction.DOWN) {
            crashed = true;
        }
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        spriteRenderer.render(renderer, animation.getSpriteDescriptor(), screenX, screenY);
        spriteRenderer.render(renderer, descriptor, screenX, screenY + TILE_SIZE);
    }

    private static final SpriteDescriptor FUSELAGE = new SpriteDescriptor(SpriteDescriptor.ANIM, 170, 0, 0, 1, 4);
    private static final SpriteDescriptor FUSELAGE_MID = FUSELAGE.withBaseIndex(174);
    private static final SpriteDescriptor FUSELAGE_RIGHT = FUSELAGE.withBaseIndex(178);
    private static final AnimationDescriptor ROTOR_ANIMATION_LEFT = new AnimationDescriptor(FUSELAGE.withBaseIndex(182), 2, 1);
    private static final AnimationDescriptor ROTOR_ANIMATION_MID = new AnimationDescriptor(FUSELAGE.withBaseIndex(190), 2, 1);
    private static final AnimationDescriptor ROTOR_ANIMATION_RIGHT = new AnimationDescriptor(FUSELAGE.withBaseIndex(200), 2, 1);
}

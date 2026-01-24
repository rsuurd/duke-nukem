package duke.gameplay.active;

import duke.gameplay.*;
import duke.gameplay.effects.Explosion;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.sfx.Sfx;

import static duke.gfx.SpriteDescriptor.ANIM;

public class Dynamite extends Active implements Updatable, SpriteRenderable, Wakeable {
    private Animation animation;

    private int timer;

    public Dynamite(int x, int y) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        animation = new Animation(DESCRIPTOR);
        timer = TIMER;
    }

    @Override
    public void update(GameplayContext context) {
        animation.tick();

        if (--timer <= 0) {
            context.getActiveManager().spawn(new ExplosionWave(getX(), getY(), Facing.LEFT));
            context.getActiveManager().spawn(new ExplosionWave(getX(), getY(), Facing.RIGHT));

            context.getSoundManager().play(Sfx.BOMB_EXPLODE);
            destroy();
        }
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    static final int TIMER = 16;
    private static final AnimationDescriptor DESCRIPTOR = new AnimationDescriptor(new SpriteDescriptor(ANIM, 116), 2, 1);

    static class ExplosionWave extends Active implements Updatable, Wakeable {
        private int timer;

        public ExplosionWave(int x, int y, Facing direction) {
            super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

            this.timer = POWER * DELAY;
            setVelocityX((direction == Facing.LEFT) ? -Level.TILE_SIZE : Level.TILE_SIZE);
        }

        @Override
        public void update(GameplayContext context) {
            if (timer % DELAY == 0) {
                context.getActiveManager().spawn(new Explosion(getX(), getY()));

                setX(getX() + getVelocityX());

                if (isBlocked(context) || isGap(context)) {
                    destroy();
                }
            }

            if (--timer <= 0) {
                destroy();
            }
        }

        private boolean isBlocked(WorldQuery query) {
            return query.isSolid(getRow(), getCol());
        }

        private boolean isGap(WorldQuery query) {
            return !query.isSolid(getRow() + 1, getCol());
        }

        static final int POWER = 5;
        static final int DELAY = 2;
    }
}

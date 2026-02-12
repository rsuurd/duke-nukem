package duke.gameplay.effects;

import duke.Renderer;
import duke.gameplay.*;
import duke.gameplay.active.Wakeable;
import duke.gfx.Renderable;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderer;
import duke.sfx.Sfx;

import static duke.level.Level.TILE_SIZE;

public class FlyingBotCrash extends Active implements Updatable, Physics, Collidable, Renderable, Wakeable {
    private SpriteDescriptor top;
    private SpriteDescriptor bottom;

    private boolean crashed;

    public FlyingBotCrash(int x, int y, Facing facing, int velocityX) {
        super(x, y, TILE_SIZE, TILE_SIZE);

        top = new SpriteDescriptor(SpriteDescriptor.ANIM, (facing == Facing.LEFT) ? 0 : 5);
        bottom = top.withBaseIndex(6);

        setVelocityX(velocityX);
        setVelocityY(-TILE_SIZE);

        crashed = false;
    }

    @Override
    public void onCollision(Direction direction, int flags) {
        crashed = true;
    }

    @Override
    public void update(GameplayContext context) {
        context.getActiveManager().spawn(EffectsFactory.createSmoke(getX(), getY()));

        if (crashed) {
            context.getSoundManager().play(Sfx.BOX_EXPLODE);
            context.getActiveManager().spawn(EffectsFactory.createSparks(getX(), getY()));
            context.getActiveManager().spawn(EffectsFactory.createParticles(getX(), getY()));

            destroy();
        }
    }

    @Override
    public int getVerticalAcceleration() {
        return GRAVITY;
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        spriteRenderer.render(renderer, top, screenX, screenY);
        spriteRenderer.render(renderer, bottom, screenX, screenY + getHeight());
    }
}

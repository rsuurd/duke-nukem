package duke.gameplay.active.items;

import duke.Renderer;
import duke.gameplay.Bolt;
import duke.gameplay.Collidable;
import duke.gameplay.GameplayContext;
import duke.gameplay.Shootable;
import duke.gameplay.effects.PoppedBalloon;
import duke.gfx.*;
import duke.level.Level;
import duke.sfx.Sfx;

import static duke.gfx.SpriteDescriptor.OBJECTS;

public class Balloon extends Item implements Collidable, Renderable, Shootable {
    private Animation animation;
    private boolean popped;

    public Balloon(int x, int y) {
        this(x, y, ItemBehaviorFactory.bonus(10000, Sfx.GET_BALLOON));
    }

    Balloon(int x, int y, ItemBehavior behavior) {
        super(x, y, null, behavior);

        animation = new Animation(BALLOON_STRING);
        popped = false;
        setVelocityY(-1);
    }

    @Override
    public void onCollision(Direction direction) {
        popped = direction == Direction.UP;
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        popped = true;
    }

    @Override
    public void update(GameplayContext context) {
        if (popped) {
            context.getActiveManager().spawn(new PoppedBalloon(getX(), getY() - 1));
            destroy();
        } else {
            super.update(context);
            animation.tick();
        }
    }

    @Override
    public int getVerticalAcceleration() {
        return 0;
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        spriteRenderer.render(renderer, BALLOON, screenX, screenY);
        spriteRenderer.render(renderer, animation.getSpriteDescriptor(), screenX, screenY + Level.TILE_SIZE);
    }

    private static final SpriteDescriptor BALLOON = new SpriteDescriptor(OBJECTS, 69);
    private static final AnimationDescriptor BALLOON_STRING =
            new AnimationDescriptor(new SpriteDescriptor(OBJECTS, 70), 3, 4);

}

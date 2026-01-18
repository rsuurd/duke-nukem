package duke.gameplay.active.enemies;

import duke.gameplay.*;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;import duke.sfx.Sfx;

public class BouncingMine extends Active implements Updatable, Physics, Collidable, Damaging, Shootable, SpriteRenderable {
    private boolean bounce;

    public BouncingMine(int x, int y) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);
    }

    @Override
    public void onCollision(Direction direction) {
        bounce = direction == Direction.DOWN;
    }

    @Override
    public void update(GameplayContext context) {
        if (bounce) {
            context.getSoundManager().play(Sfx.MINE_BOUNCE);
            setVelocityY(BOUNCE_SPEED);
            bounce = false;
        }
    }

    @Override
    public int getVerticalAcceleration() {
        return GRAVITY;
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {}

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return DESCRIPTOR;
    }

    static final int BOUNCE_SPEED = -15;

    private static final SpriteDescriptor DESCRIPTOR = new SpriteDescriptor(SpriteDescriptor.ANIM, 231, 0, 0, 1, 1);
}

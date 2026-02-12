package duke.gameplay.active.enemies;

import duke.gameplay.*;
import duke.gameplay.active.Wakeable;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;

import java.util.Map;

import static duke.gfx.SpriteDescriptor.OBJECTS;
import static duke.level.Level.HALF_TILE_SIZE;

public class EnemyFire extends Active implements Updatable, Physics, Damaging, SpriteRenderable, Collidable, Wakeable {
    private Animation animation;
    private SpriteDescriptor descriptor;

    public EnemyFire(int x, int y, Facing facing) {
        super(x, y, Level.TILE_SIZE, HALF_TILE_SIZE);

        animation = new Animation(DESCRIPTORS.get(facing));
        descriptor = (facing == Facing.LEFT ? MUZZLE_FLASH_LEFT : MUZZLE_FLASH_RIGHT);

        setVelocityX(facing == Facing.LEFT ? -HALF_TILE_SIZE : HALF_TILE_SIZE);
    }

    @Override
    public void update(GameplayContext context) {
        descriptor = animation.getSpriteDescriptor();
        animation.tick();
    }

    @Override
    public void onCollision(Direction direction, int flags) {
        destroy();
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return descriptor;
    }

    @Override
    public int getVerticalAcceleration() {
        return 0;
    }

    private static final SpriteDescriptor MUZZLE_FLASH_LEFT = new SpriteDescriptor(OBJECTS, 48, 0, -4, 1, 1);
    private static final SpriteDescriptor MUZZLE_FLASH_RIGHT = new SpriteDescriptor(OBJECTS, 49, 0, -4, 1, 1);
    private static final SpriteDescriptor SHOT_LEFT = new SpriteDescriptor(OBJECTS, 39, 0, -4, 1, 1);
    private static final SpriteDescriptor SHOT_RIGHT = new SpriteDescriptor(OBJECTS, 41, 0, -4, 1, 1);
    private static final Map<Facing, AnimationDescriptor> DESCRIPTORS = Map.of(
            Facing.LEFT, new AnimationDescriptor(SHOT_LEFT, 2, 1),
            Facing.RIGHT, new AnimationDescriptor(SHOT_RIGHT, 2, 1)
    );
}

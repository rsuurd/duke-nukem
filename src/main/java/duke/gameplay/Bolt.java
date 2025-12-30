package duke.gameplay;

import duke.gameplay.effects.EffectsFactory;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.resources.AssetManager;

import static duke.gameplay.Facing.LEFT;
import static duke.gameplay.Facing.RIGHT;

public class Bolt extends Active implements Updatable, SpriteRenderable {
    private Animation animation;

    private Facing facing;
    private int flash = 1;

    public Bolt(int x, int y, Facing facing) {
        super(x, y, Level.TILE_SIZE, 2);

        this.facing = facing;

        if (facing == Facing.LEFT) {
            animation = new Animation(MUZZLE_FLASH_LEFT);
        } else {
            animation = new Animation(MUZZLE_FLASH_RIGHT);
        }
    }

    @Override
    public void update(GameplayContext context) {
        setX(getX() + getVelocityX());

        if (isFarAway(context.getPlayer())) {
            deactivate();
        } else if (collides(context.getLevel())) {
            onHit(context);
        }

        updateAnimation();
    }

    @Override
    public int getVelocityX() {
        return SPEED * ((facing == RIGHT) ? 1 : -1);
    }

    private boolean isFarAway(Player player) {
        int distance = Math.abs(player.getX() - getX());

        return distance >= Level.TILE_SIZE * 8;
    }

    private boolean collides(Level level) {
        int row = getY() / Level.TILE_SIZE;
        int col = getProbeX() / Level.TILE_SIZE;

        return level.isSolid(row, col);
    }

    private int getProbeX() {
        int remainder = (facing == LEFT) ? getX() % Level.TILE_SIZE : 0;

        return getX() + remainder;
    }

    private void updateAnimation() {
        if (flash == 0) {
            animation.setAnimation(BOLT);
        } else {
            flash--;
        }

        animation.tick();
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    private void onHit(GameplayContext context) {
        deactivate();
        int hitX = getX() + (8 * ((facing == LEFT) ? 1 : -1));
        context.getActiveManager().spawn(EffectsFactory.createSparks(hitX, getY() - 4));
    }

    public static Bolt create(Player player) {
        int x = player.getX();
        int y = player.getY() + 13;

        return new Bolt(x, y, player.getFacing());
    }

    static final int SPEED = Level.TILE_SIZE;
    private static SpriteDescriptor BASE = new SpriteDescriptor(AssetManager::getObjects, 6, 0, -10, 1, 1);
    private static AnimationDescriptor MUZZLE_FLASH_LEFT = new AnimationDescriptor(BASE.withBaseIndex(46), 1, 1);
    private static AnimationDescriptor MUZZLE_FLASH_RIGHT = new AnimationDescriptor(BASE.withBaseIndex(47), 1, 1);
    private static AnimationDescriptor BOLT = new AnimationDescriptor(BASE.withBaseIndex(6), 4, 1);
}

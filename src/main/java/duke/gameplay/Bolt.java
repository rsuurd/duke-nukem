package duke.gameplay;

import duke.gameplay.effects.EffectsFactory;
import duke.gameplay.player.Player;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.resources.AssetManager;
import duke.sfx.Sfx;

import java.util.function.Predicate;

import static duke.gameplay.Facing.LEFT;
import static duke.level.Level.TILE_SIZE;

public class Bolt extends Active implements Updatable, SpriteRenderable {
    private Facing facing;
    private int flash = 1;

    private SpriteDescriptor spriteDescriptor;
    private Animation animation;

    public Bolt(int x, int y, Facing facing) {
        super(x, y, TILE_SIZE, 8);

        this.facing = facing;
        spriteDescriptor = (facing == LEFT) ? MUZZLE_FLASH_LEFT : MUZZLE_FLASH_RIGHT;
        animation = new Animation(BOLT);

        setVelocityX(facing == LEFT ? -SPEED : SPEED);

        activate();
    }

    @Override
    public void update(GameplayContext context) {
        setX(getX() + getVelocityX());

        if (isFarAway(context.getPlayer())) {
            destroy();
        } else {
            checkCollision(context);
        }

        updateAnimation();
    }

    private boolean isFarAway(Player player) {
        int distance = Math.abs(player.getX() - getX());

        return distance >= TILE_SIZE * 10;
    }

    private void checkCollision(GameplayContext context) {
        if (collidesWithTile(context, getY(), IS_SOLID)) return;
        if (collidesWithTile(context, getY() + getHeight() - 1, DESTRUCTIBLE_BRICKS)) return;

        collidesWithShootable(context);
    }

    private boolean collidesWithTile(GameplayContext context, int y, Predicate<Integer> check) {
        int row = y / TILE_SIZE;
        int col = getProbeX() / TILE_SIZE;
        int tileId = context.getLevel().getTile(row, col);
        boolean collides = check.test(tileId);

        if (collides) {
            onTileHit(context, tileId, row, col);
        }

        return collides;
    }

    private int getProbeX() {
        int remainder = (facing == LEFT) ? getX() % TILE_SIZE : 0;

        return getX() + remainder;
    }

    private void onTileHit(GameplayContext context, int tileId, int row, int col) {
        if (DESTRUCTIBLE_BRICKS.test(tileId)) {
            context.getLevel().setTile(row, col, DESTROYED_BRICKS_TILE_ID);
            context.getSoundManager().play(Sfx.HIT_A_BREAKER);
        }

        spawnSparks(context);
        context.getScoreManager().score(10);

        destroy();
    }

    private void collidesWithShootable(GameplayContext context) {
        for (Active active : context.getActiveManager().getActives()) {
            if (active == this) continue;
            if (!active.isActivated()) continue;
            if (active.isDestroyed()) continue;

            if (active.intersects(this)) {
                if (active instanceof Solid solid && solid.isSolid()) {
                    spawnSparks(context);
                    destroy();
                }

                if (active instanceof Shootable shootable) {
                    shootable.onShot(context, this);
                    destroy();
                }

                return;
            }
        }
    }

    private void spawnSparks(GameplayContext context) {
        // TODO offset X position based on facing
        context.getActiveManager().spawn(EffectsFactory.createSparks(getX(), getY()));
    }

    private void updateAnimation() {
        if (flash == 0) {
            animation.tick();
            spriteDescriptor = animation.getSpriteDescriptor();
        } else {
            flash--;
        }
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return spriteDescriptor;
    }

    public static Bolt create(Player player) {
        int x = player.getX();
        int y = player.getY() + 13;

        return new Bolt(x, y, player.getFacing());
    }

    static final int SPEED = TILE_SIZE;
    static final int DESTRUCTIBLE_BRICKS_TILE_ID = 0x1800;
    static final int DESTROYED_BRICKS_TILE_ID = 0x17e0;
    private static final Predicate<Integer> IS_SOLID = Level::isSolid;
    private static final Predicate<Integer> DESTRUCTIBLE_BRICKS = (tileId) -> tileId == DESTRUCTIBLE_BRICKS_TILE_ID;
    private static final SpriteDescriptor BASE = new SpriteDescriptor(AssetManager::getObjects, 6, 0, -10, 1, 1);
    private static final SpriteDescriptor MUZZLE_FLASH_LEFT = BASE.withBaseIndex(46);
    private static final SpriteDescriptor MUZZLE_FLASH_RIGHT = BASE.withBaseIndex(47);
    private static final AnimationDescriptor BOLT = new AnimationDescriptor(BASE.withBaseIndex(6), 4, 1);
}

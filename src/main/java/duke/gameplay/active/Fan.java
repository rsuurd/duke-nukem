package duke.gameplay.active;

import duke.gameplay.*;
import duke.gameplay.effects.EffectsFactory;
import duke.gameplay.player.Player;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.sfx.Sfx;

import static duke.gfx.AnimationDescriptor.Type.ONE_SHOT;
import static duke.level.Level.TILE_SIZE;

public class Fan extends Active implements Updatable, SpriteRenderable, Shootable {
    private Facing facing;
    private Animation animation;

    private boolean destroyed;
    private Rectangle reach;

    public Fan(int x, int y, Facing facing) {
        this(x, y, facing, new Animation(SPINNING));
    }

    Fan(int x, int y, Facing facing, Animation animation) {
        super(x, y, TILE_SIZE, 2 * TILE_SIZE);

        this.facing = facing;
        this.animation = animation;

        destroyed = false;
        reach = new Rectangle(x + ((facing == Facing.RIGHT) ? getWidth() : -REACH), y, REACH, getHeight());
    }

    @Override
    public void update(GameplayContext context) {
        pushPlayer(context.getPlayer());

        animation.tick();
    }

    private void pushPlayer(Player player) {
        if (destroyed) return;

        if (player.intersects(reach)) {
            int pushX = (facing == Facing.RIGHT) ? STRENGTH : -STRENGTH;

            // TODO we should push by adding to the player's velocity.
            // However that requires rework of the player's input and friction code. Will do that later
            player.setX(player.getX() + pushX);
        }
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        if (!destroyed) {
            context.getScoreManager().score(1000);
        }

        destroyed = true; // keep our own flag since we want to keep it around
        animation.setAnimation(DESTROYED);
        if (animation.isFinished()) {
            animation.reset();
        }

        context.getActiveManager().spawn(EffectsFactory.createSmoke(getX(), getY()));
        context.getSoundManager().play(Sfx.SMALL_DEATH);
    }

    private static final SpriteDescriptor DESCRIPTOR = new SpriteDescriptor(SpriteDescriptor.ANIM, 162, 0, 0, 2, 1);
    private static final AnimationDescriptor SPINNING = new AnimationDescriptor(DESCRIPTOR, 4, 1);
    private static final AnimationDescriptor DESTROYED = new AnimationDescriptor(DESCRIPTOR, 4, 4, ONE_SHOT);

    static final int REACH = 3 * TILE_SIZE;
    static final int STRENGTH = 8;
}

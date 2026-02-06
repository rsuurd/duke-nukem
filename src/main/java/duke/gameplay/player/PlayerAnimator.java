package duke.gameplay.player;

import duke.gameplay.Facing;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;

import static duke.gfx.SpriteDescriptor.MAN;

public class PlayerAnimator implements SpriteRenderable {
    private Animation walkAnimation;

    private SpriteDescriptor currentDescriptor;

    public PlayerAnimator() {
        walkAnimation = new Animation(WALKING_RIGHT);
    }

    public void animate(Player player) {
        State state = player.getState();
        Facing facing = player.getFacing();

        if (player.getHealth().isDamageTaken()) {
            animateHit(facing);
            return;
        }

        switch (state) {
            case STANDING -> animateStationary(player, facing);
            case JUMPING -> animateJump(facing);
            case FALLING -> animateFall(facing);
            case WALKING -> animateWalk(facing);
        }
    }

    private void animateHit(Facing facing) {
        currentDescriptor = (facing == Facing.LEFT) ? XRAY_LEFT : XRAY_RIGHT;
    }

    private void animateStationary(Player player, Facing facing) {
        if (player.getWeapon().isTriggered()) {
            currentDescriptor = facing == Facing.LEFT ? SHOOT_LEFT : SHOOT_RIGHT;
        } else {
            currentDescriptor = (facing == Facing.LEFT) ? STANDING_LEFT : STANDING_RIGHT;
        }
    }

    private void animateJump(Facing facing) {
        // if shoes and non zero X velocity, randomly decide to flip or not

        currentDescriptor = (facing == Facing.LEFT) ? JUMPING_LEFT : JUMPING_RIGHT;
    }

    private void animateFall(Facing facing) {
        // if flipping, just continue that animation until finished

        currentDescriptor = (facing == Facing.LEFT) ? FALLING_LEFT : FALLING_RIGHT;
    }

    private void animateWalk(Facing facing) {
        walkAnimation.tick();
        walkAnimation.setAnimation(facing == Facing.LEFT ? WALKING_LEFT : WALKING_RIGHT);
        currentDescriptor = walkAnimation.getSpriteDescriptor();
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return currentDescriptor;
    }

    private static final SpriteDescriptor BASE_DESCRIPTOR = new SpriteDescriptor(MAN, 0, -8, 0, 2, 2);
    private static final SpriteDescriptor STANDING_LEFT = BASE_DESCRIPTOR.withBaseIndex(48);
    private static final SpriteDescriptor STANDING_RIGHT = BASE_DESCRIPTOR.withBaseIndex(52);
    private static final AnimationDescriptor WALKING_LEFT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(0), 4, 2);
    private static final AnimationDescriptor WALKING_RIGHT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(16), 4, 2);
    private static final SpriteDescriptor JUMPING_LEFT = BASE_DESCRIPTOR.withBaseIndex(32);
    private static final SpriteDescriptor JUMPING_RIGHT = BASE_DESCRIPTOR.withBaseIndex(36);
    private static final AnimationDescriptor FLIPPING_LEFT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(56), 7, 2, AnimationDescriptor.Type.ONE_SHOT);
    private static final AnimationDescriptor FLIPPING_RIGHT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(84), 7, 2, AnimationDescriptor.Type.ONE_SHOT);
    private static final SpriteDescriptor FALLING_LEFT = BASE_DESCRIPTOR.withBaseIndex(40);
    private static final SpriteDescriptor FALLING_RIGHT = BASE_DESCRIPTOR.withBaseIndex(44);
    private static final SpriteDescriptor SHOOT_LEFT = BASE_DESCRIPTOR.withBaseIndex(12);
    private static final SpriteDescriptor SHOOT_RIGHT = BASE_DESCRIPTOR.withBaseIndex(28);
    private static final SpriteDescriptor XRAY_LEFT = BASE_DESCRIPTOR.withBaseIndex(176);
    private static final SpriteDescriptor XRAY_RIGHT = BASE_DESCRIPTOR.withBaseIndex(180);
}

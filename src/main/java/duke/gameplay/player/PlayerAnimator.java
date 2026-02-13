package duke.gameplay.player;

import duke.gameplay.Facing;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;

import static duke.gfx.SpriteDescriptor.MAN;

public class PlayerAnimator implements SpriteRenderable {
    private Animation animation;
    private Animation flipAnimation;

    private SpriteDescriptor currentDescriptor;

    public PlayerAnimator() {
        animation = new Animation(WALKING_RIGHT);
        flipAnimation = new Animation(FLIPPING_RIGHT);
    }

    public void animate(Player player) {
        if (player.getHealth().isDamageTaken()) {
            animateHit(player.getFacing());
            return;
        }

        switch (player.getState()) {
            case STANDING -> animateStationary(player);
            case JUMPING -> animateJump(player);
            case FALLING -> animateFall(player);
            case WALKING -> animateWalk(player);
            case CLINGING -> animateClinging(player);
            case PULLING_UP -> animatePullUp(player);
        }

        if (player.isGrounded()) {
            flipAnimation.reset();
        }
    }

    private void animateHit(Facing facing) {
        currentDescriptor = (facing == Facing.LEFT) ? XRAY_LEFT : XRAY_RIGHT;
    }

    private void animateStationary(Player player) {
        if (player.getWeapon().isTriggered()) {
            currentDescriptor = (player.getFacing() == Facing.LEFT) ? SHOOT_LEFT : SHOOT_RIGHT;
        } else {
            currentDescriptor = (player.getFacing() == Facing.LEFT) ? STANDING_LEFT : STANDING_RIGHT;
        }
    }

    private void animateJump(Player player) {
        if (player.isFlipping()) {
            animateFlip(player.getFacing());
        } else {
            currentDescriptor = (player.getFacing() == Facing.LEFT) ? JUMPING_LEFT : JUMPING_RIGHT;
        }
    }

    private void animateFall(Player player) {
        if (player.isFlipping() && !flipAnimation.isFinished()) {
            animateFlip(player.getFacing());
        } else {
            currentDescriptor = (player.getFacing() == Facing.LEFT) ? FALLING_LEFT : FALLING_RIGHT;
        }
    }

    private void animateFlip(Facing facing) {
        if (flipAnimation.isReset()) { // start of the flip, determine facing
            flipAnimation.setAnimation(facing == Facing.LEFT ? FLIPPING_LEFT : FLIPPING_RIGHT);
        }

        currentDescriptor = flipAnimation.getSpriteDescriptor();
        flipAnimation.tick();
    }

    private void animateWalk(Player player) {
        animation.setAnimation((player.getFacing() == Facing.LEFT) ? WALKING_LEFT : WALKING_RIGHT);
        currentDescriptor = animation.getSpriteDescriptor();
        animation.tick();
    }

    private void animateClinging(Player player) {
        if (!player.isMoving() && (currentDescriptor == CLINGING_SHOOT_LEFT || currentDescriptor == CLINGING_SHOOT_RIGHT))
            return;

        if (player.getWeapon().isTriggered()) {
            currentDescriptor = (player.getFacing() == Facing.LEFT) ? CLINGING_SHOOT_LEFT : CLINGING_SHOOT_RIGHT;
        } else {
            animation.setAnimation((player.getFacing() == Facing.LEFT) ? CLINGING_LEFT : CLINGING_RIGHT);
            currentDescriptor = animation.getSpriteDescriptor();

            if (player.isMoving()) {
                animation.tick();
            }
        }
    }

    private void animatePullUp(Player player) {
        animation.setAnimation((player.getFacing() == Facing.LEFT) ? PULLING_UP_LEFT : PULLING_UP_RIGHT);
        currentDescriptor = animation.getSpriteDescriptor();
        animation.tick();
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
    private static final AnimationDescriptor CLINGING_LEFT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(112), 4, 2);
    private static final AnimationDescriptor CLINGING_RIGHT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(128), 4, 2);
    private static final SpriteDescriptor CLINGING_SHOOT_LEFT = BASE_DESCRIPTOR.withBaseIndex(144);
    private static final SpriteDescriptor CLINGING_SHOOT_RIGHT = BASE_DESCRIPTOR.withBaseIndex(148);
    private static final AnimationDescriptor PULLING_UP_LEFT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(152), 3, 1);
    private static final AnimationDescriptor PULLING_UP_RIGHT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(164), 3, 1);
}

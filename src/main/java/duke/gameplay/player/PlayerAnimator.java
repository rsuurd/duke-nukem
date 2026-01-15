package duke.gameplay.player;

import duke.gameplay.Facing;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;

import static duke.gfx.SpriteDescriptor.MAN;

public class PlayerAnimator implements SpriteRenderable {
    private Animation animation;
    private SpriteDescriptor currentDescriptor;

    public PlayerAnimator() {
        animation = new Animation(WALKING_RIGHT);
    }

    public void animate(Player player) {
        State state = player.getState();
        Facing facing = player.getFacing();

        if (player.getHealth().isDamageTaken()) {
            currentDescriptor = (facing == Facing.LEFT) ? XRAY_LEFT : XRAY_RIGHT;

            return;
        }

        switch (state) {
            case STANDING -> {
                if (player.getWeapon().isTriggered()) {
                    currentDescriptor = facing == Facing.LEFT ? SHOOT_LEFT : SHOOT_RIGHT;
                } else {
                    currentDescriptor = (facing == Facing.LEFT) ? STANDING_LEFT : STANDING_RIGHT;
                }
            }
            case JUMPING -> currentDescriptor = (facing == Facing.LEFT) ? JUMPING_LEFT : JUMPING_RIGHT;
            case FALLING -> currentDescriptor = (facing == Facing.LEFT) ? FALLING_LEFT : FALLING_RIGHT;
            case WALKING -> {
                animation.tick();
                animation.setAnimation(facing == Facing.LEFT ? WALKING_LEFT : WALKING_RIGHT);
                currentDescriptor = animation.getSpriteDescriptor();
            }
        }
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return currentDescriptor;
    }

    private static SpriteDescriptor BASE_DESCRIPTOR = new SpriteDescriptor(MAN, 0, -8, 0, 2, 2);
    private static SpriteDescriptor STANDING_LEFT = BASE_DESCRIPTOR.withBaseIndex(50);
    private static SpriteDescriptor STANDING_RIGHT = BASE_DESCRIPTOR.withBaseIndex(54);
    private static AnimationDescriptor WALKING_LEFT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(0), 4, 2);
    private static AnimationDescriptor WALKING_RIGHT = new AnimationDescriptor(BASE_DESCRIPTOR.withBaseIndex(16), 4, 2);
    private static SpriteDescriptor JUMPING_LEFT = BASE_DESCRIPTOR.withBaseIndex(32);
    private static SpriteDescriptor JUMPING_RIGHT = BASE_DESCRIPTOR.withBaseIndex(36);
    private static SpriteDescriptor FALLING_LEFT = BASE_DESCRIPTOR.withBaseIndex(40);
    private static SpriteDescriptor FALLING_RIGHT = BASE_DESCRIPTOR.withBaseIndex(44);
    private static SpriteDescriptor SHOOT_LEFT = BASE_DESCRIPTOR.withBaseIndex(12);
    private static SpriteDescriptor SHOOT_RIGHT = BASE_DESCRIPTOR.withBaseIndex(28);
    private static SpriteDescriptor XRAY_LEFT = BASE_DESCRIPTOR.withBaseIndex(182);
    private static SpriteDescriptor XRAY_RIGHT = BASE_DESCRIPTOR.withBaseIndex(186);
}

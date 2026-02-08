package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.Active;
import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.Updatable;
import duke.gameplay.player.Player;
import duke.gfx.*;

import java.util.List;

import static duke.level.Level.TILE_SIZE;

public class ConveyorBelt extends Active implements Updatable, Renderable {
    private Facing direction;

    private List<Animation> animations;

    public ConveyorBelt(int x, int y, int width, Facing direction) {
        super(x, y, width, TILE_SIZE);

        this.direction = direction;

        animations = List.of(
                new Animation(LEFT),
                new Animation(CENTER),
                new Animation(RIGHT)
        );

        if (this.direction == Facing.LEFT) {
            reverseAnimation();
        }
    }

    private void reverseAnimation() {
        for (Animation animation : animations) {
            animation.reverse();
        }
    }

    @Override
    public void update(GameplayContext context) {
        pushPlayer(context.getPlayer());

        updateAnimation();
    }

    private void updateAnimation() {
        for (Animation animation : animations) {
            animation.tick();
        }
    }

    private void pushPlayer(Player player) {
        if (hasOnTop(player)) {
            // TODO we should push by adding to the player's velocity.
            // However that requires rework of the player's input and friction code. Will do that later

            int pushX = (direction == Facing.RIGHT) ? SPEED : -SPEED;
            player.setX(player.getX() + pushX);
        }
        // TODO what about gappling on the underside? We'll cross that bridge when we get there.
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        // To consider: we could overwrite the tiles in the level to simulate the animation instead of doing it like this.
        // for now, just overlay
        for (int conveyorX = 0; conveyorX < getWidth(); conveyorX += TILE_SIZE) {
            Animation animation = getAnimation(conveyorX);

            spriteRenderer.render(renderer, animation.getSpriteDescriptor(), screenX + conveyorX, screenY);
        }
    }

    private Animation getAnimation(int conveyorX) {
        if (conveyorX == 0) {
            return animations.getFirst();
        } else if (conveyorX >= getWidth() - TILE_SIZE) {
            return animations.getLast();
        } else {
            return animations.get(1);
        }
    }

    public Facing getDirection() {
        return direction;
    }

    static final int SPEED = 8;

    public static final int SOLID_LEFT_CONVEYOR_TILE_ID = 0x1b80;
    public static final int SOLID_CENTER_CONVEYOR_TILE_ID = 0x1c00;
    public static final int SOLID_RIGHT_CONVEYOR_TILE_ID = 0x1c40;

    private static final AnimationDescriptor LEFT = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.TILES, SOLID_LEFT_CONVEYOR_TILE_ID / 0x20), 4, 1);
    private static final AnimationDescriptor CENTER = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.TILES, SOLID_CENTER_CONVEYOR_TILE_ID / 0x20), 2, 1);
    private static final AnimationDescriptor RIGHT = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.TILES, SOLID_RIGHT_CONVEYOR_TILE_ID / 0x20), 4, 1);
}

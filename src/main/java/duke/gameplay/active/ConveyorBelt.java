package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.*;
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
        pushPlayer(context);

        updateAnimation();
    }

    private void updateAnimation() {
        for (Animation animation : animations) {
            animation.tick();
        }
    }

    // TODO we should push by adding to the player's velocity.
    // However that requires rework of the player's input and friction code. Will do that later
    private void pushPlayer(WorldQuery query) {
        Player player = query.getPlayer();

        // TODO add  || isGrappledOn(player) to support grappling
        if (hasOnTop(player)) {
            int deltaX = (direction == Facing.RIGHT) ? SPEED : -SPEED;

            if (canPush(query, deltaX)) {
                player.setX(player.getX() + deltaX);
            }
        }
    }

    // this check can be removed if we just increase the player's velocity
    private boolean canPush(WorldQuery query, int deltaX) {
        Player player = query.getPlayer();
        int row = query.getPlayer().getRow();
        // the destination col, taking onto account the player's size (see Elevator)
        int col = (player.getX() + (deltaX > 0 ? player.getWidth() - 1 : 0) + deltaX) / TILE_SIZE;
        return !query.isSolid(row, col);
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

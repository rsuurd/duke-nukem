package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.*;
import duke.gameplay.player.Player;
import duke.gfx.*;

import static duke.level.Level.TILE_SIZE;

public class ConveyorBelt extends Active implements Updatable, Solid, Renderable {
    private Facing direction;

    private Animation leftEdgeAnimation;
    private Animation centerAnimation;
    private Animation rightEdgeAnimation;

    public ConveyorBelt(int x, int y, int width, Facing direction) {
        super(x, y, width, TILE_SIZE);

        this.direction = direction;

        leftEdgeAnimation = new Animation(LEFT);
        centerAnimation = new Animation(CENTER);
        rightEdgeAnimation = new Animation(RIGHT);

        if (direction == Facing.LEFT) {
            leftEdgeAnimation.reverse();
            centerAnimation.reverse();
            rightEdgeAnimation.reverse();
        }
    }

    @Override
    public void update(GameplayContext context) {
        pushPlayer(context.getPlayer());

        leftEdgeAnimation.tick();
        centerAnimation.tick();
        rightEdgeAnimation.tick();
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
        int conveyorX = screenX;

        spriteRenderer.render(renderer, leftEdgeAnimation.getSpriteDescriptor(), conveyorX, screenY);

        for (conveyorX = conveyorX + TILE_SIZE; conveyorX < screenX + getWidth() - TILE_SIZE; conveyorX += TILE_SIZE) {
            spriteRenderer.render(renderer, centerAnimation.getSpriteDescriptor(), conveyorX, screenY);
        }

        spriteRenderer.render(renderer, rightEdgeAnimation.getSpriteDescriptor(), conveyorX, screenY);
    }

    public Facing getDirection() {
        return direction;
    }

    private static final AnimationDescriptor LEFT = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.TILES, 220), 4, 1);
    private static final AnimationDescriptor CENTER = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.TILES, 224), 2, 1);
    private static final AnimationDescriptor RIGHT = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.TILES, 226), 4, 1);

    static final int SPEED = 8;
}

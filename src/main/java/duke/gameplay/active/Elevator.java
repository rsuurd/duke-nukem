package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.*;
import duke.gameplay.player.Player;
import duke.gfx.Renderable;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderer;
import duke.level.Level;
import duke.sfx.Sfx;

import static duke.gfx.SpriteDescriptor.OBJECTS;
import static duke.gfx.SpriteDescriptor.TILES;

public class Elevator extends Active implements Renderable, Solid, Updatable, Interactable {
    private int elevation;

    public Elevator(int x, int y) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        elevation = 0;
    }

    @Override
    public boolean canInteract(Player player) {
        return hasOnTop(player);
    }

    @Override
    public void interactRequested(GameplayContext context) {
        if (canMoveUp(context.getPlayer(), context)) {
            moveUp();
            push(context.getPlayer());

            context.getSoundManager().play(Sfx.ELEVATOR);
        }
    }

    @Override
    public void update(GameplayContext context) {
        if (!hasOnTop(context.getPlayer())) {
            moveDown();
        }
    }

    private boolean canMoveUp(Player player, WorldQuery query) {
        int rowAbove = player.getRow() - 1;
        int leftCol = player.getCol();
        int rightCol = (player.getX() + player.getWidth() - 1) / Level.TILE_SIZE;

        return !(query.isSolid(rowAbove, leftCol) || query.isSolid(rowAbove, rightCol));
    }

    private void moveUp() {
        changeElevation(1);
    }

    private void moveDown() {
        if (elevation > 0) {
            changeElevation(-1);
        }
    }

    private void changeElevation(int delta) {
        elevation += delta;

        setY(getY() - (delta * Level.TILE_SIZE));
    }

    private void push(Physics body) {
        body.setY(getY() - body.getHeight());
    }

    @Override
    public int getHeight() {
        return (1 + elevation) * Level.TILE_SIZE;
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        spriteRenderer.render(renderer, ELEVATOR_TOP_DESCRIPTOR, screenX, screenY);

        for (int y = screenY + Level.TILE_SIZE; y < (screenY + getHeight()); y += Level.TILE_SIZE) {
            spriteRenderer.render(renderer, ELEVATOR_BASE_DESCRIPTOR, screenX, y);
        }
    }

    private static final SpriteDescriptor ELEVATOR_TOP_DESCRIPTOR = new SpriteDescriptor(OBJECTS, 5);
    private static final SpriteDescriptor ELEVATOR_BASE_DESCRIPTOR = new SpriteDescriptor(TILES, 215);
}

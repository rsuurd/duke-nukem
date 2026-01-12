package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.*;
import duke.gfx.Renderable;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderer;
import duke.level.Level;
import duke.resources.AssetManager;

public class Elevator extends Active implements Renderable, Solid, Updatable {
    private int elevation;

    public Elevator(int x, int y) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        elevation = 0;
    }

    @Override
    public void update(GameplayContext context) {
        Player player = context.getPlayer();
        boolean onElevator = hasOnTop(player);

        if (onElevator && player.isUsing() && canMoveUp(player, context)) {
            moveUp();
            push(player);
//            context.getSoundManager().play(Sfx.ELEVATOR);
        } else if (!onElevator) {
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

    private static final SpriteDescriptor ELEVATOR_TOP_DESCRIPTOR = new SpriteDescriptor(AssetManager::getObjects, 5, 0, 0, 1, 1);
    private static final SpriteDescriptor ELEVATOR_BASE_DESCRIPTOR = new SpriteDescriptor(AssetManager::getTiles, 215, 0, 0, 1, 1);
}

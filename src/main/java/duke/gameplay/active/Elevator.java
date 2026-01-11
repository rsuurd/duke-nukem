package duke.gameplay.active;

import duke.gameplay.*;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.resources.AssetManager;

public class Elevator extends Active implements SpriteRenderable, Solid, Updatable {
    private int elevation;

    public Elevator(int x, int y) {
        super(x, y, Level.TILE_SIZE, Level.TILE_SIZE);

        elevation = 0;
    }

    @Override
    public void update(GameplayContext context) {
        Player player = context.getPlayer();
        boolean onElevator = hasOnTop(player);

        if (onElevator && player.isUsing() && canMoveUp(player, context.getLevel())) {
            moveUp();
            push(player);
//            context.getSoundManager().play(Sfx.ELEVATOR);
        } else if (!onElevator) {
            moveDown();
        }
    }

    private boolean canMoveUp(Player player, Level level) {
        int rowAbove = player.getRow() - 1;
        int leftCol = player.getCol();
        int rightCol = (player.getX() + player.getWidth() - 1) / Level.TILE_SIZE;

        return !(level.isSolid(rowAbove, leftCol) || level.isSolid(rowAbove, rightCol));
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
    public SpriteDescriptor getSpriteDescriptor() {
        return SPRITE_DESCRIPTOR;
    }

    @Override
    public int getHeight() {
        return (1 + elevation) * Level.TILE_SIZE;
    }

    // TODO custom height based rendering

    private static final SpriteDescriptor SPRITE_DESCRIPTOR = new SpriteDescriptor(AssetManager::getObjects, 5, 0, 0, 1, 1);
}

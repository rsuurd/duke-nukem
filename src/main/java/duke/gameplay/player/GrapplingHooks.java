package duke.gameplay.player;

import duke.gameplay.Facing;
import duke.gameplay.WorldQuery;
import duke.level.Flags;
import duke.level.Level;
import duke.ui.KeyHandler;

public class GrapplingHooks {
    private boolean ready;

    public GrapplingHooks() {
        ready = true;
    }

    public void update(WorldQuery query, KeyHandler.Input input) {
        Player player = query.getPlayer();

        if (!player.getInventory().isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)) return;
        ready |= player.isGrounded();

        checkToCling(query);

        if (query.getPlayer().getState() != State.CLINGING) return;

        pullUpIfRequestedAndPossible(query, input);
        checkRelease(query, input);
    }

    private void checkToCling(WorldQuery query) {
        Player player = query.getPlayer();
        if (!ready || player.isGrounded() || player.getState() == State.CLINGING) return;

        int row = (player.getY() - 1) / Level.TILE_SIZE;
        if (Flags.CLINGABLE.isSet(query.getTileFlags(row, player.getCol()))) {
            player.cling();
            ready = false;
        }
    }

    private void pullUpIfRequestedAndPossible(WorldQuery query, KeyHandler.Input input) {
        if (input.up() && canPullUp(query)) {
            query.getPlayer().pullUp();
        }
    }

    private boolean canPullUp(WorldQuery query) {
        Player player = query.getPlayer();

        int topRow = player.getRow() - PULL_UP_OFFSET;
        int bottomRow = ((player.getY() + player.getHeight() - 1) / Level.TILE_SIZE) - PULL_UP_OFFSET;
        int leftCol = player.getCol();
        int rightCol = (player.getX() + player.getWidth() - 1) / Level.TILE_SIZE;

        for (int row = topRow; row <= bottomRow; row++) {
            for (int col = leftCol; col <= rightCol; col++) {
                if (query.isSolid(row, col)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void checkRelease(WorldQuery query, KeyHandler.Input input) {
        Player player = query.getPlayer();
        int rowAbove = player.getRow() - 1;
        int col = (player.getX() + ((player.getFacing() == Facing.RIGHT) ? (player.getWidth()) - 1 : 0)) / Level.TILE_SIZE;

        if (input.down() || player.getHealth().isDamageTaken() || !Flags.CLINGABLE.isSet(query.getTileFlags(rowAbove, col))) {
            player.releaseCling();
            ready = false;
        }
    }

    private static final int PULL_UP_OFFSET = 3;
}

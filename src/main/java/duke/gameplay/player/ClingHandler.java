package duke.gameplay.player;

import duke.gameplay.Facing;
import duke.gameplay.WorldQuery;
import duke.level.Flags;
import duke.level.Level;
import duke.ui.KeyHandler;

public class ClingHandler {
    public void onBump(Player player, int flags) {
        if (!player.getInventory().isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)) return;
        if (!Flags.CLINGABLE.isSet(flags)) return;

        player.cling();
    }

    public void update(WorldQuery query, KeyHandler.Input input) {
        if (query.getPlayer().getState() != State.CLINGING) return;

        pullUpIfRequestedAndPossible(query, input);
        checkRelease(query, input);
    }

    private void checkRelease(WorldQuery query, KeyHandler.Input input) {
        Player player = query.getPlayer();
        int rowAbove = player.getRow() - 1;
        int col = (player.getX() + ((player.getFacing() == Facing.RIGHT) ? (player.getWidth()) - 1 : 0)) / Level.TILE_SIZE;

        if (input.down() || player.getHealth().isDamageTaken() || !Flags.CLINGABLE.isSet(query.getTileFlags(rowAbove, col))) {
            player.releaseCling();
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

    private static final int PULL_UP_OFFSET = 3;
}

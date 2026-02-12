package duke.gameplay.player;

import duke.gameplay.WorldQuery;
import duke.level.Flags;
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
        int col = player.getCol(); // TODO check left and right col as well?

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
        // check tiles above the ceiling (player.getRow() - 2 & 3 ), player left col + player right col
        return false;
    }
}

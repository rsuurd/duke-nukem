package duke.gameplay.player;

import duke.gameplay.WorldQuery;
import duke.level.Flags;

public class ClingHandler {
    public void onBump(Player player, int flags) {
        if (!player.getInventory().isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS)) return;
        if (!Flags.CLINGABLE.isSet(flags)) return;

        player.cling();
    }

    public void update(WorldQuery query) {
        if (query.getPlayer().getState() != State.CLINGING) return;

        checkPullUp(query);
        checkRelease(query);
    }

    private void checkRelease(WorldQuery query) {
        Player player = query.getPlayer();
        int rowAbove = player.getRow() - 1;
        int col = player.getCol(); // depends on facing;

        if (!Flags.CLINGABLE.isSet(query.getTileFlags(rowAbove, col))) {
            player.releaseCling();
        }

        // does the player release when taking damage?
        // does the player release jump pressed?
    }

    private void checkPullUp(WorldQuery query) {
        Player player = query.getPlayer();

        if (!player.isUsing()) return;

        // if the play can occupy the tile above the clingable tile, then allow them to pull up
    }
}

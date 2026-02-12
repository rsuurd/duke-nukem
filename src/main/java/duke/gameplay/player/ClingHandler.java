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

        checkPullUp(query);
        checkRelease(query, input);
    }

    private void checkRelease(WorldQuery query, KeyHandler.Input input) {
        Player player = query.getPlayer();
        int rowAbove = player.getRow() - 1;
        int col = player.getX() + (player.getFacing() == Facing.RIGHT ? player.getWidth() - 1 : 0) / Level.TILE_SIZE;

        if (input.down() || player.getHealth().isDamageTaken() || !Flags.CLINGABLE.isSet(query.getTileFlags(rowAbove, col))) {
            player.releaseCling();
        }
    }

    private void checkPullUp(WorldQuery query) {
        Player player = query.getPlayer();

        if (!player.isUsing()) return;


        // if the player can occupy the tile above the clingable tile, then allow them to pull up
        // pull up if possible
        // set state to pulling_up
        // disable controls
        // update y position until pull up is complete
        // enable controls again

    }
}

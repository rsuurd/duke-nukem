package duke.gameplay.player;

import duke.gameplay.Facing;
import duke.ui.KeyHandler;

import static duke.gameplay.player.Player.MAX_SPEED;
import static duke.gameplay.player.Player.SPEED;

public class MovementHandler {
    public void handleInput(Player player, KeyHandler.Input input) {
        if (input.left() == input.right()) {
            slowDown(player);
        } else if (input.left()) {
            move(player, Facing.LEFT);
        } else {
            move(player, Facing.RIGHT);
        }
    }

    private void move(Player player, Facing facing) {
        if (player.getFacing() == facing) {
            accelerate(player);
        } else {
            player.setFacing(facing);
        }

        // maybe rename to move, update moving boolean in player?
        player.walk();
    }

    private void accelerate(Player player) {
        int accelerationX = (player.getFacing() == Facing.RIGHT) ? SPEED : -SPEED;
        int newVelocityX = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, player.getVelocityX() + accelerationX));

        player.setVelocityX(newVelocityX);
    }

    private void slowDown(Player player) {
        int decelerationX = (player.getVelocityX() > 0) ? -SPEED : SPEED;
        int newVelocityX = Math.abs(player.getVelocityX()) < SPEED ? 0 : player.getVelocityX() + decelerationX;

        player.setVelocityX(newVelocityX);

        if (newVelocityX == 0 && player.getState() == State.WALKING) {
            // maybe rename to stop? Let it check current state and decide whether to stand or not?
            player.stand();
        }
    }
}

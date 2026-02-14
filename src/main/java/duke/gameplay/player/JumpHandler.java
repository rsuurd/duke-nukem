package duke.gameplay.player;

import duke.ui.KeyHandler;

import java.util.Random;

import static duke.gameplay.Physics.GRAVITY;

public class JumpHandler {
    private Random random;

    private boolean jumpReady;
    private int jumpTicks;

    public JumpHandler(Random random) {
        this(random, true, 0);
    }

    JumpHandler(Random random, boolean jumpReady, int jumpTicks) {
        this.random = random;
        this.jumpReady = jumpReady;
        this.jumpTicks = jumpTicks;
    }

    public void handleInput(Player player, KeyHandler.Input input) {
        if (!input.jump()) {
            jumpReady = player.isGrounded();

            return;
        }

        if (!jumpReady) return;
        if (!player.isGrounded()) return;

        boolean hasBoots = player.getInventory().isEquippedWith(Inventory.Equipment.BOOTS);

        player.setVelocityY(hasBoots ? HIGH_JUMP_POWER : JUMP_POWER);
        jumpReady = false;
        jumpTicks = JUMP_TICKS;

        boolean flipping = hasBoots && player.getVelocityX() != 0 && random.nextBoolean();

        player.jump(flipping);
    }

    public void update(Player player) {
        if (player.getState() != State.JUMPING) return;

        if (player.getHealth().isDamageTaken()) {
            player.fastFall();

            return;
        }

        if (jumpTicks > 0) {
            jumpTicks--;
        } else {
            player.slowFall();
        }
    }

    // TODO this should probably check the vertical acceleration based on jumpTicks instead of velocityY
    public int getVerticalAcceleration(Player player) {
        boolean hasBoots = player.getInventory().isEquippedWith(Inventory.Equipment.BOOTS);

        // apex early if no boots
        if (!hasBoots && player.getVelocityY() >= -3) {
            return -player.getVelocityY();
        }

        // Don't let it exceed velocity in order to have velocityY settle at 0 before falling again
        return Math.min(GRAVITY, -player.getVelocityY());
    }

    static final int JUMP_POWER = -13;
    static final int HIGH_JUMP_POWER = JUMP_POWER - 2;
    static final int JUMP_TICKS = 9;
}

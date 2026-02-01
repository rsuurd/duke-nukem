package duke.gameplay.active.enemies.behavior;

import duke.gameplay.WorldQuery;
import duke.gameplay.active.enemies.Enemy;
import duke.gameplay.active.enemies.FlyingBot;
import duke.gameplay.player.Player;

import static duke.gameplay.Facing.LEFT;
import static duke.gameplay.Facing.RIGHT;

public class FlyingBotBehavior implements EnemyBehavior {
    private int tick;
    private int interval;

    private int turning;
    private int shootCooldown;

    public FlyingBotBehavior() {
        this(TURNED_LEFT);
    }

    FlyingBotBehavior(int turning) {
        tick = 0;
        interval = 4;
        this.turning = turning;
        shootCooldown = 0;
    }

    @Override
    public void behave(WorldQuery worldQuery, Enemy enemy) {
        enemy.setVelocityX(0);

        Player player = worldQuery.getPlayer();
        moveVertically(enemy, player);

        if (shouldBehave()) {
            facePlayer(enemy, player);

            if (canChasePlayer()) {
                chasePlayer(enemy, player);
            }
        }

        shootIfAble(enemy);

        tick = (tick + 1) % interval;
    }

    private boolean shouldBehave() {
        return tick == 0;
    }

    private void facePlayer(Enemy enemy, Player player) {
        int turnOffset = Integer.signum(player.getX() - enemy.getX());
        enemy.setFacing(turnOffset > 0 ? RIGHT : LEFT);
        turning = Math.max(0, Math.min(5, turning + turnOffset));

        if (enemy instanceof FlyingBot flyingBot) {
            flyingBot.setRotationFrame(turning);
        }
    }

    private boolean canChasePlayer() {
        return !isTurning();
    }

    private boolean isTurning() {
        return turning > TURNED_LEFT && turning < TURNED_RIGHT;
    }

    private void chasePlayer(Enemy enemy, Player player) {
        moveHorizontally(enemy, player);
    }

    private void moveHorizontally(Enemy enemy, Player player) {
        int direction = Integer.signum(player.getX() - enemy.getX());

        enemy.setVelocityX(direction * HORIZONTAL_SPEED);
    }

    private void moveVertically(Enemy enemy, Player player) {
        int direction = Integer.signum(player.getY() - enemy.getY());

        enemy.setVelocityY(direction * VERTICAL_SPEED);
    }

    private void shootIfAble(Enemy enemy) {
        if (shootCooldown > 0) {
            shootCooldown--;
        }

        if (!isTurning() && shootCooldown == 0) {
            enemy.shoot();

            shootCooldown = SHOOT_COOLDOWN;
        }
    }

    static final int TURNED_LEFT = 0;
    static final int TURNED_RIGHT = 5;
    static final int HORIZONTAL_SPEED = 8;
    static final int VERTICAL_SPEED = 1;
    static final int SHOOT_COOLDOWN = 24;
}

package duke.gameplay.active.enemies.behavior;

import duke.gameplay.Facing;
import duke.gameplay.WorldQuery;
import duke.gameplay.active.enemies.Enemy;
import duke.gameplay.player.Player;

import static duke.gameplay.active.enemies.behavior.Ed209Behavior.State.*;

public class Ed209Behavior implements EnemyBehavior {
    private State current;
    private State next;

    private int timer;

    public Ed209Behavior() {
        this(IDLE, SHOOT);
    }

    Ed209Behavior(State initial, State next) {
        this.current = initial;
        this.next = next;

        timer = 0;
    }

    @Override
    public void behave(WorldQuery worldQuery, Enemy enemy) {
        timer++;

        switch (current) {
            case IDLE -> idle();
            case SHOOT -> shoot(enemy);
            case JUMP -> jump(enemy);
        }

        tryFacePlayer(worldQuery.getPlayer(), enemy);
    }

    private void tryFacePlayer(Player player, Enemy enemy) {
        if (enemy.getVelocityY() < 0) return;

        int enemyCenterX = enemy.getX() + (enemy.getWidth() / 2);
        int playerCenterX = player.getX() + (player.getWidth() / 2);

        if (enemyCenterX < playerCenterX) {
            enemy.setFacing(Facing.RIGHT);
        } else {
            enemy.setFacing(Facing.LEFT);
        }
    }

    private void idle() {
        if (timer >= IDLE_TICKS) {
            switchState(next, IDLE);
        }
    }

    private void shoot(Enemy enemy) {
        enemy.shoot();
        switchState(IDLE, JUMP);
    }

    private void jump(Enemy enemy) {
        if (timer == 1) { // first time a jump update is handle the timer will have increased
            enemy.jump();
        }

        if (enemy.isGrounded()) {
            switchState(State.IDLE, SHOOT);
        }
    }

    private void switchState(State newState, State next) {
        current = newState;
        this.next = next;

        timer = 0;
    }

    public State getState() {
        return current;
    }

    public enum State {
        IDLE,
        SHOOT,
        JUMP
    }

    static final int IDLE_TICKS = 10;
}

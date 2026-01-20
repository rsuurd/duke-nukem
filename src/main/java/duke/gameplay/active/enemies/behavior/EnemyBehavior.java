package duke.gameplay.active.enemies.behavior;

import duke.gameplay.WorldQuery;
import duke.gameplay.active.enemies.Enemy;

public interface EnemyBehavior {
    void behave(WorldQuery worldQuery, Enemy enemy);
}

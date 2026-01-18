package duke.gameplay.active.enemies;

import duke.gameplay.Active;
import duke.gameplay.WorldQuery;

public interface EnemyBehavior {
    void behave(WorldQuery worldQuery, Active enemy);
}

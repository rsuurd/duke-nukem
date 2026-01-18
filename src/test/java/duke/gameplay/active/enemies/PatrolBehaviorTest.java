package duke.gameplay.active.enemies;

import duke.gameplay.Active;
import duke.gameplay.WorldQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class PatrolBehaviorTest {
    private PatrolBehavior behavior;

    private WorldQuery worldQuery;
    private Active active;

    @BeforeEach
    void createBehavior() {
        behavior = new PatrolBehavior(duke.gameplay.Facing.RIGHT, 1, 1);

        worldQuery = mock();
        active = mock();
    }

    @Test
    void shouldPatrol() {
        when(worldQuery.isSolid(anyInt(), anyInt())).thenReturn(false, true);

        behavior.behave(worldQuery, active);

        verify(active).setX(1);
    }

    @Test
    void shouldTurnAroundWhenSolid() {
        when(worldQuery.isSolid(anyInt(), anyInt())).thenReturn(true);

        behavior.behave(worldQuery, active);

        verify(active).setX(-1);
    }

    @Test
    void shouldTurnAroundWhenGapReached() {
        when(worldQuery.isSolid(anyInt(), anyInt())).thenReturn(false, false);

        behavior.behave(worldQuery, active);

        verify(active).setX(-1);
    }

    @Test
    void shouldBehaveAtInterval() {
        int interval = 3;
        behavior = new PatrolBehavior(duke.gameplay.Facing.RIGHT, interval, 1);

        for (int i = 0; i < interval; i++) {
            behavior.behave(worldQuery, active);
        }

        verify(active, times(1)).setX(anyInt());
    }
}

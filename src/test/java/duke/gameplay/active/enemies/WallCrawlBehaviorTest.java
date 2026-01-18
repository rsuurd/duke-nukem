package duke.gameplay.active.enemies;

import duke.gameplay.Facing;
import duke.gameplay.WorldQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class WallCrawlBehaviorTest {
    private WallCrawlBehavior behavior;

    private WorldQuery worldQuery;
    private WallCrawler active;

    @BeforeEach
    void createBehavior() {
        behavior = new WallCrawlBehavior(Facing.RIGHT, 1, 1);

        worldQuery = mock();
        active = mock();
    }

    @Test
    void shouldPatrol() {
        when(worldQuery.isSolid(anyInt(), anyInt())).thenReturn(false, true);

        behavior.behave(worldQuery, active);

        verify(active).setY(-1);
    }

    @Test
    void shouldTurnAroundWhenSolid() {
        when(worldQuery.isSolid(anyInt(), anyInt())).thenReturn(true);

        behavior.behave(worldQuery, active);

        verify(active).setY(1);
        verify(active).reverse();
    }

    @Test
    void shouldTurnAroundWhenGapReached() {
        when(worldQuery.isSolid(anyInt(), anyInt())).thenReturn(false, false);

        behavior.behave(worldQuery, active);

        verify(active).setY(1);
        verify(active).reverse();
    }
}
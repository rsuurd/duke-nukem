package duke.gameplay.active.enemies.behavior;

import duke.gameplay.Facing;
import duke.gameplay.WorldQuery;
import duke.gameplay.active.enemies.WallCrawler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class WallCrawlBehaviorTest {
    private WallCrawlBehavior behavior;

    private WorldQuery worldQuery;
    private WallCrawler crawler;

    @BeforeEach
    void createBehavior() {
        behavior = new WallCrawlBehavior(Facing.RIGHT, 1, 1);

        worldQuery = mock();
        crawler = mock();
    }

    @Test
    void shouldPatrol() {
        when(worldQuery.isSolid(anyInt(), anyInt())).thenReturn(false, true);

        behavior.behave(worldQuery, crawler);

        verify(crawler).setY(-1);
    }

    @Test
    void shouldReverseWhenSolid() {
        when(worldQuery.isSolid(anyInt(), anyInt())).thenReturn(true);

        behavior.behave(worldQuery, crawler);

        verify(crawler).setY(1);
        verify(crawler).reverse();
    }

    @Test
    void shouldReverseWhenGapReached() {
        when(worldQuery.isSolid(anyInt(), anyInt())).thenReturn(false, false);

        behavior.behave(worldQuery, crawler);

        verify(crawler).setY(1);
        verify(crawler).reverse();
    }
}

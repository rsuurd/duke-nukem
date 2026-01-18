package duke.gameplay.active.enemies;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.effects.Effect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class WallcrawlerTest {
    private EnemyBehavior behavior;
    private GameplayContext context;

    private WallCrawler wallCrawler;

    @BeforeEach
    void create() {
        behavior = mock();
        context = GameplayContextFixture.create();

        wallCrawler = new WallCrawler(0, 0, Facing.RIGHT, behavior);
    }

    @Test
    void shouldBehave() {
        wallCrawler.update(context);

        verify(behavior).behave(context, wallCrawler);
    }

    @Test
    void shouldBeDamaging() {
        assertThat(wallCrawler.getDamage()).isGreaterThan(0);
    }

    @Test
    void shouldBeShot() {
        wallCrawler.onShot(context, null);

        assertTrue(wallCrawler.isDestroyed());
        verify(context.getActiveManager()).spawn(isA(Effect.class));
        verify(context.getScoreManager()).score(100);
    }
}

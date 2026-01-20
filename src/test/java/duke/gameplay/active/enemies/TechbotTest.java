package duke.gameplay.active.enemies;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.effects.TechbotDestruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TechbotTest {
    private GameplayContext context;
    private EnemyBehavior behavior;

    private Techbot techbot;

    @BeforeEach
    void create() {
        context = GameplayContextFixture.create();
        behavior = mock();
        techbot = new Techbot(0, 0, behavior);
    }

    @Test
    void shouldBehave() {
        techbot.update(context);

        verify(behavior).behave(context, techbot);
    }

    @Test
    void shouldBeDamaging() {
        assertThat(techbot.getDamage()).isGreaterThan(0);
    }

    @Test
    void shouldBeShot() {
        techbot.onShot(context, null);

        assertTrue(techbot.isDestroyed());
        verify(context.getActiveManager()).spawn(isA(TechbotDestruction.class));
    }
}

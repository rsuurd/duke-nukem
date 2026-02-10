package duke.gameplay.active.enemies;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.Health;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlameElementalTest {
    @Mock
    private EnemyBehavior behavior;

    @Mock
    private Health health;

    private FlameElemental flameElemental;

    private GameplayContext context;

    @BeforeEach
    void create() {
        flameElemental = new FlameElemental(0, 0, Facing.RIGHT, behavior, health);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBehave() {
        flameElemental.update(context);

        verify(behavior).behave(context, flameElemental);
    }

    @Test
    void shouldShouldBeImmuneToDamage() {
        flameElemental.onShot(context, mock());

        verifyNoInteractions(health);
        assertThat(flameElemental.isDestroyed()).isFalse();
    }
}

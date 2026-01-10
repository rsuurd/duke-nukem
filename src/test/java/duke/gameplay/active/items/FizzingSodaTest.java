package duke.gameplay.active.items;

import duke.gameplay.Collidable;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.Effect;
import duke.level.Level;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FizzingSodaTest {
    @Test
    void shouldFlyUp() {
        FizzingSoda item = new FizzingSoda(0, 0, mock(), mock());

        assertThat(item.getVelocityY()).isEqualTo(-Level.HALF_TILE_SIZE);
        assertThat(item.getVerticalAcceleration()).isEqualTo(0);
    }

    @Test
    void shouldBeDestroyedOnImpact() {
        GameplayContext context = GameplayContextFixture.create();
        FizzingSoda item = new FizzingSoda(0, 0, mock(), mock());

        item.onCollision(Collidable.Direction.UP);
        item.update(context);

        assertThat(item.isDestroyed()).isTrue();
        verify(context.getActiveManager()).spawn(isA(Effect.class));
    }
}

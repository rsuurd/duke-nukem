package duke.gameplay.effects;

import duke.gameplay.Collidable;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static duke.gameplay.GameplayContextFixture.SOLID_TILE_FLAG;
import static duke.gameplay.Physics.GRAVITY;
import static duke.level.Level.TILE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class FlyingBotCrashTest {
    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldCrash() {
        FlyingBotCrash crash = new FlyingBotCrash(100, 200, duke.gameplay.Facing.LEFT, -50);

        assertThat(crash.getVelocityX()).isEqualTo(-50);
        assertThat(crash.getVelocityY()).isEqualTo(-TILE_SIZE);
        assertThat(crash.getVerticalAcceleration()).isEqualTo(GRAVITY);

        crash.update(context);

        verify(context.getActiveManager()).spawn(isA(Effect.class));
    }

    @ParameterizedTest
    @EnumSource(Collidable.Direction.class)
    void shouldExplodeOnCollision(Collidable.Direction direction) {
        FlyingBotCrash crash = new FlyingBotCrash(100, 200, duke.gameplay.Facing.LEFT, -50);

        crash.onCollision(direction, SOLID_TILE_FLAG);
        crash.update(context);

        verify(context.getSoundManager()).play(duke.sfx.Sfx.BOX_EXPLODE);
        verify(context.getActiveManager(), times(2)).spawn(isA(Effect.class));
        verify(context.getActiveManager()).spawn(anyList());
        assertThat(crash.isDestroyed()).isTrue();
    }
}
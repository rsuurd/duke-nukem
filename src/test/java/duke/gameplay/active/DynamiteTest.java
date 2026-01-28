package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.Explosion;
import duke.level.Level;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static duke.gameplay.active.Dynamite.ExplosionWave.DELAY;
import static duke.gameplay.active.Dynamite.TIMER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DynamiteTest {
    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = spy(GameplayContextFixture.create());
    }

    @Test
    void shouldExplode() {
        Dynamite dynamite = new Dynamite(0, 0);

        for (int timer = TIMER; timer > 0; timer--) {
            dynamite.update(context);
        }

        verify(context.getActiveManager(), times(2)).spawn(any(Dynamite.ExplosionWave.class));
        verify(context.getSoundManager()).play(Sfx.BOMB_EXPLODE);

        assertThat(dynamite.isDestroyed()).isTrue();
    }

    @Test
    void shouldPropagateExplosionWaveUntilPowerDepletes() {
        // check solid/gap check is done with same isSolid check
        when(context.isSolid(anyInt(), anyInt())).thenReturn(false, true, false, true, false, true, false, true, false, true);

        Dynamite.ExplosionWave wave = new Dynamite.ExplosionWave(0, 0, duke.gameplay.Facing.RIGHT);
        assertThat(wave.isAwake()).isTrue();

        int expectedX = wave.getX();
        for (int i = Dynamite.ExplosionWave.POWER * Dynamite.ExplosionWave.DELAY; i > 0; i--) {
            assertThat(wave.isDestroyed()).isFalse();

            wave.update(context);

            expectedX += (i % DELAY == 0) ? Level.TILE_SIZE : 0;
            assertThat(wave.getX()).isEqualTo(expectedX);
        }

        verify(context.getActiveManager(), times(Dynamite.ExplosionWave.POWER)).spawn(any(Explosion.class));

        assertThat(wave.isDestroyed()).isTrue();
    }

    @Test
    void shouldStopPropagationWhenHittingSolid() {
        when(context.isSolid(anyInt(), anyInt())).thenReturn(true);

        Dynamite.ExplosionWave wave = new Dynamite.ExplosionWave(0, 0, duke.gameplay.Facing.RIGHT);

        wave.update(context);

        assertThat(wave.isDestroyed()).isTrue();
    }

    @Test
    void shouldStopPropagationWhenEncounteringGap() {
        when(context.isSolid(anyInt(), anyInt())).thenReturn(false);

        Dynamite.ExplosionWave wave = new Dynamite.ExplosionWave(0, 0, duke.gameplay.Facing.RIGHT);

        wave.update(context);

        assertThat(wave.isDestroyed()).isTrue();
    }
}

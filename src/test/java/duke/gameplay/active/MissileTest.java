package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.Effect;
import duke.level.Flags;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static duke.gameplay.GameplayContextFixture.SOLID_TILE_FLAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class MissileTest {
    private Missile missile;

    private GameplayContext context;

    @BeforeEach
    void create() {
        missile = new Missile(0, 0);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldIdle() {
        missile.update(context);

        assertThat(missile.getX()).isEqualTo(0);
        assertThat(missile.getY()).isEqualTo(0);
    }

    @Test
    void shouldIgniteWhenShot() {
        missile.onShot(context, mock());

        verify(context.getActiveManager(), times(2)).spawn(isA(Effect.class));
        verify(context.getSoundManager()).play(Sfx.ROCKET);
        verify(context.getScoreManager()).score(200);
    }

    @Test
    void shouldDestroyBottomTileWhenLaunched() {
        when(context.getLevel().getTile(anyInt(), anyInt())).thenReturn(32);

        completeIgnition();

        verify(context.getLevel()).setTile(4, 0, 32);
        verify(context.getActiveManager()).spawn(anyList());
    }

    @Test
    void shouldFlyWhenLaunched() {
        completeIgnition();

        assertThat(missile.getY()).isEqualTo(-Missile.SPEED);

        verify(context.getActiveManager(), atLeastOnce()).spawn(isA(Effect.class));
    }

    @Test
    void shouldDestroyTileOnImpact() {
        when(context.getLevel().getTileFlags(anyInt(), anyInt())).thenReturn(SOLID_TILE_FLAG);
        when(context.getLevel().getTile(anyInt(), anyInt())).thenReturn(64);

        completeIgnition();

        verify(context.getLevel()).setTile(4, 0, 64);
        verify(context.getActiveManager(), atLeastOnce()).spawn(anyList());
    }

    private void completeIgnition() {
        missile.onShot(context, mock());

        for (int i = 0; i < Missile.IGNITION_DURATION; i++) {
            missile.update(context);
        }
    }
}

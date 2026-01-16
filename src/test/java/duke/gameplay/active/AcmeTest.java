package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.Rectangle;
import duke.gameplay.effects.Effect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class AcmeTest {
    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldIdle() {
        Acme acme = new Acme(0, 0);

        acme.update(context);

        assertThat(acme.isIdle()).isTrue();
        assertThat(acme.isShaking()).isFalse();
        assertThat(acme.isFalling()).isFalse();
    }

    @Test
    void shouldShake() {
        Acme acme = new Acme(0, 0);

        wakeUp(acme);

        assertThat(acme.isIdle()).isFalse();
        assertThat(acme.isShaking()).isTrue();
        assertThat(acme.isFalling()).isFalse();
    }

    @Test
    void shouldFall() {
        Acme acme = new Acme(0, 0);
        wakeUp(acme);
        fastForward(acme, Acme.SHAKE_TIME);

        assertThat(acme.isIdle()).isFalse();
        assertThat(acme.isShaking()).isFalse();
        assertThat(acme.isFalling()).isTrue();

        assertThat(acme.getY()).isEqualTo(12);
    }

    @Test
    void shouldCrash() {
        Acme acme = new Acme(0, 0);
        wakeUp(acme);
        fastForward(acme, Acme.SHAKE_TIME);
        when(context.getLevel().isSolid(anyInt(), anyInt())).thenReturn(true);

        acme.update(context);

        assertThat(acme.isDestroyed()).isTrue();
        verify(context.getActiveManager()).spawn(any(Effect.class));
        verify(context.getActiveManager()).spawn(any(Effect.class));
    }

    @Test
    void shouldBeShot() {
        Acme acme = new Acme(0, 0);

        acme.onShot(context, mock());

        assertThat(acme.isDestroyed()).isTrue();
        verify(context.getScoreManager()).score(500, 8, 0);
        verify(context.getActiveManager(), times(2)).spawn(any(Effect.class));
    }

    private void wakeUp(Acme acme) {
        when(context.getPlayer().intersects(any(Rectangle.class))).thenReturn(true);

        acme.update(context);
    }

    private void fastForward(Acme acme, int ticks) {
        for (int timer = 0; timer < ticks; timer++) {
            acme.update(context);
        }
    }
}

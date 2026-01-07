package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.Rectangle;
import duke.sfx.Sfx;
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
        assertThat(acme.isDetaching()).isFalse();
        assertThat(acme.isFalling()).isFalse();
    }

    @Test
    void shouldShake() {
        Acme acme = new Acme(0, 0);

        when(context.getPlayer().intersects(any(Rectangle.class))).thenReturn(true);

        acme.update(context);

        assertThat(acme.isIdle()).isFalse();
        assertThat(acme.isShaking()).isTrue();
        assertThat(acme.isDetaching()).isFalse();
        assertThat(acme.isFalling()).isFalse();
    }

    @Test
    void shouldDetach() {
        Acme acme = new Acme(0, 0);
        fastForward(acme, Acme.SHAKE_TIME);

        acme.update(context);

        assertThat(acme.isIdle()).isFalse();
        assertThat(acme.isShaking()).isFalse();
        assertThat(acme.isDetaching()).isTrue();
        assertThat(acme.isFalling()).isFalse();

        verify(context.getSoundManager()).play(Sfx.DANGER_SIGN);
    }

    @Test
    void shouldFall() {
        Acme acme = new Acme(0, 0);
        fastForward(acme, Acme.DETACH_TIME);

        acme.update(context);

        assertThat(acme.isIdle()).isFalse();
        assertThat(acme.isShaking()).isFalse();
        assertThat(acme.isDetaching()).isFalse();
        assertThat(acme.isFalling()).isTrue();
    }

    @Test
    void shouldCrash() {
        Acme acme = new Acme(0, 0);
        fastForward(acme, Acme.DETACH_TIME);
        when(context.getLevel().isSolid(anyInt(), anyInt())).thenReturn(true);

        acme.update(context);

        assertThat(acme.isActive()).isFalse();
    }

    @Test
    void shouldBeShot() {
        Acme acme = new Acme(0, 0);

        acme.onShot(context, mock());

        assertThat(acme.isActive()).isFalse();
        verify(context.getScoreManager()).score(500, 8, 0);
    }

    private void fastForward(Acme acme, int ticks) {
        when(context.getPlayer().intersects(any(Rectangle.class))).thenReturn(true);

        for (int timer = -1; timer < ticks; timer++) {
            acme.update(context);
        }
    }
}

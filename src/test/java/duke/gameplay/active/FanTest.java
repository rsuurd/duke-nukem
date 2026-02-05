package duke.gameplay.active;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.Rectangle;
import duke.gameplay.effects.Effect;
import duke.gfx.Animation;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FanTest {
    @Mock
    private Animation animation;

    private Fan fan;

    private GameplayContext context;

    @BeforeEach
    void create() {
        fan = new Fan(0, 0, Facing.RIGHT, animation);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldPushPlayer() {
        when(context.getPlayer().intersects(any(Rectangle.class))).thenReturn(true);

        fan.update(context);

        verify(context.getPlayer()).setX(anyInt());
    }

    @Test
    void shouldBeShot() {
        fan.onShot(context, null);

        assertThat(fan.isDestroyed()).isFalse();
        verify(animation).setAnimation(any());
        verify(context.getActiveManager()).spawn(isA(Effect.class));
        verify(context.getSoundManager()).play(Sfx.SMALL_DEATH);
        verify(context.getScoreManager()).score(1000);
    }

    @Test
    void shouldNotPushPlayerWhenDestroyed() {
        fan.onShot(context, null);
        fan.update(context);

        verify(context.getPlayer(), never()).setX(anyInt());
    }

    @Test
    void shouldNotPushPlayerWhenPlayerOutOfReach() {
        when(context.getPlayer().intersects(any(Rectangle.class))).thenReturn(false);

        fan.update(context);

        verify(context.getPlayer(), never()).setX(anyInt());
    }

    @Test
    void shouldSpinWhenShotAgain() {
        when(animation.isFinished()).thenReturn(true);

        fan.onShot(context, null);

        verify(animation).setAnimation(any());
        verify(animation).reset();
        verify(context.getSoundManager()).play(Sfx.SMALL_DEATH);
        verify(context.getActiveManager()).spawn(isA(Effect.class));
    }
}

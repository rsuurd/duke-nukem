package duke.gameplay.active.enemies;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.Health;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.effects.Effect;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.active.enemies.FlameWheelBot.TOGGLE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlameWheelBotTest {
    @Mock
    private EnemyBehavior behavior;

    @Mock
    private Health health;

    private FlameWheelBot bot;

    private GameplayContext context;

    @BeforeEach
    void createContext() {
        bot = new FlameWheelBot(0, 0, Facing.LEFT, behavior, health);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBeDamaging() {
        assertThat(bot.getDamage()).isGreaterThan(0);
    }

    @Test
    void shouldBehave() {
        bot.update(context);

        verify(behavior).behave(context, bot);
    }

    @Test
    void shouldToggleBetweenFlameOnAndOff() {
        assertThat(bot.isFlameOn()).isFalse();

        toggleFlame(bot);

        assertThat(bot.isFlameOn()).isTrue();

        toggleFlame(bot);

        assertThat(bot.isFlameOn()).isFalse();
    }

    @Test
    void shouldNotBeDamagedWhenShotAndFlameIsOn() {
        toggleFlame(bot);

        bot.onShot(context, mock());

        verify(health, never()).takeDamage(1);
    }

    @Test
    void shouldBeDamagedWhenShotAndFlameIsOff() {
        bot.onShot(context, mock());

        verify(health).takeDamage(1);
    }

    @Test
    void shouldVisualizeDamage() {
        when(health.getMax()).thenReturn(2);
        when(health.getCurrent()).thenReturn(1);

        for (int i = 0; i < 4; i++) {
            bot.update(context);
        }

        verify(context.getActiveManager()).spawn(isA(Effect.class));
    }

    @Test
    void shouldBeDestroyed() {
        when(health.isDead()).thenReturn(true);

        bot.onShot(context, mock());

        assertThat(bot.isDestroyed()).isTrue();
        verify(context.getScoreManager()).score(5000);
        verify(context.getActiveManager(), atLeastOnce()).spawn(any(Effect.class));
        verify(context.getSoundManager()).play(Sfx.BOX_EXPLODE);
    }

    private void toggleFlame(FlameWheelBot bot) {
        for (int i = 0; i < TOGGLE_TIME; i++) {
            bot.update(context);
        }
    }
}
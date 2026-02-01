package duke.gameplay.active.enemies;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.player.PlayerHealth;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.Facing.LEFT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlyingBotTest {
    @Mock
    private EnemyBehavior behavior;

    private FlyingBot flyingBot;

    private GameplayContext context;

    @BeforeEach
    void createContext() {
        flyingBot = new FlyingBot(0, 0, LEFT, behavior);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBehave() {
        flyingBot.update(context);

        verify(behavior).behave(context, flyingBot);
    }

    @Test
    void shouldBeDestroyedAndHurtOnCollisionWithPlayer() {
        PlayerHealth health = mock();
        when(context.getPlayer().getHealth()).thenReturn(health);
        when(context.getPlayer().intersects(flyingBot)).thenReturn(true);

        flyingBot.update(context);

        assertThat(flyingBot.isDestroyed()).isTrue();
        verify(health).takeDamage(1);
        verify(context.getScoreManager()).score(200);
        verify(context.getSoundManager()).play(Sfx.SMALL_DEATH);
    }

    @Test
    void shouldBeDestroyedWhenStompedFromAbove() {
        when(context.getPlayer().getVelocityY()).thenReturn(8);
        when(context.getPlayer().intersects(flyingBot)).thenReturn(true);

        flyingBot.update(context);

        assertThat(flyingBot.isDestroyed()).isTrue();
        verify(context.getPlayer(), never()).getHealth();
        verify(context.getScoreManager()).score(200);
        verify(context.getSoundManager()).play(Sfx.SMALL_DEATH);
    }

    @Test
    void shouldSpawnEnemyFireWhenShootRequested() {
        flyingBot.shoot();

        flyingBot.update(context);

        verify(context.getActiveManager()).spawn(isA(EnemyFire.class));
        verify(context.getSoundManager()).play(Sfx.ENEMY_SHOT);
    }
}
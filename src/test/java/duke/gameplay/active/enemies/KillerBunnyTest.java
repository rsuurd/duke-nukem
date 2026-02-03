package duke.gameplay.active.enemies;

import duke.gameplay.BonusTracker;
import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.effects.KillerBunnySpin;
import duke.gameplay.player.PlayerHealth;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KillerBunnyTest {
    private KillerBunny bunny;

    @Mock
    private EnemyBehavior behavior;

    private GameplayContext context;

    @BeforeEach
    void create() {
        bunny = new KillerBunny(0, 0, Facing.LEFT, behavior);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBehave() {
        bunny.update(context);

        verify(behavior).behave(context, bunny);
    }

    @Test
    void shouldNotBehaveWhileTurning() {
        bunny.onFacingChanged(Facing.RIGHT);

        bunny.update(context);

        verifyNoInteractions(behavior);
    }

    @Test
    void shouldNotBeAbleToMoveWhileTurning() {
        assertThat(bunny.isAbleToMove()).isTrue();

        bunny.onFacingChanged(Facing.RIGHT);

        assertThat(bunny.isAbleToMove()).isFalse();

        bunny.update(context);

        assertThat(bunny.isAbleToMove()).isTrue();
    }

    @Test
    void shouldSpinAroundOnPlayerCollision() {
        PlayerHealth health = mock();
        when(context.getPlayer().getHealth()).thenReturn(health);
        when(context.getPlayer().intersects(bunny)).thenReturn(true);

        bunny.update(context);

        assertThat(bunny.isDestroyed()).isTrue();
        verify(context.getBonusTracker()).trackDestroyed(BonusTracker.Type.BUNNY);
        verify(health).takeDamage(1);
        verify(context.getSoundManager()).play(Sfx.RABBIT_GONE);
        verify(context.getActiveManager()).spawn(isA(KillerBunnySpin.class));
    }

    @Test
    void shouldBeDestroyedOnShot() {
        bunny.onShot(context, mock());

        assertThat(bunny.isDestroyed()).isTrue();
        verify(context.getBonusTracker()).trackDestroyed(BonusTracker.Type.BUNNY);
        verify(context.getSoundManager()).play(Sfx.HIT_A_BREAKER);
        verify(context.getActiveManager()).spawn(anyList());
    }
}
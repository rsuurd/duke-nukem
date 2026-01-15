package duke.gameplay.active;

import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.BlinkingEffect;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

class FlamethrowerTest {
    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldIdle() {
        Flamethrower flamethrower = new Flamethrower(0, 0, Facing.LEFT);
        assertThat(flamethrower.getState()).isEqualTo(Flamethrower.State.IDLE);
    }

    @Test
    void shouldIgniteAfterIdle() {
        Flamethrower flamethrower = new Flamethrower(0, 0, Flamethrower.State.IDLE);

        fastForward(flamethrower);

        assertThat(flamethrower.getState()).isEqualTo(Flamethrower.State.IGNITE);
        verify(context.getSoundManager()).play(Sfx.TORCH_ON);
        verify(context.getActiveManager()).spawn(isA(BlinkingEffect.class));
    }

    @Test
    void shouldBurnAfterIgnite() {
        Flamethrower flamethrower = new Flamethrower(0, 0, Flamethrower.State.IGNITE);

        fastForward(flamethrower);

        assertThat(flamethrower.getState()).isEqualTo(Flamethrower.State.BURN);
        verify(context.getActiveManager()).spawn(isA(Flamethrower.Flame.class));
    }

    @Test
    void shouldGoBackToIdleAfterBurn() {
        Flamethrower flamethrower = new Flamethrower(0, 0, Flamethrower.State.BURN);

        fastForward(flamethrower);

        assertThat(flamethrower.getState()).isEqualTo(Flamethrower.State.IDLE);
    }

    private void fastForward(Flamethrower flamethrower) {
        for (int i = 0; i <= flamethrower.getState().duration; i++) {
            flamethrower.update(context);
        }
    }

    @Test
    void flameShouldDamage() {
        Flamethrower.Flame flame = new Flamethrower.Flame(0, 0, Facing.LEFT, 10);

        assertThat(flame.getDamage()).isEqualTo(1);
    }

    @Test
    void flameShouldExpireAfterDuration() {
        Flamethrower.Flame flame = new Flamethrower.Flame(0, 0, Facing.LEFT, 3);

        for (int i = 0; i < 3; i++) {
            assertThat(flame.isDestroyed()).isFalse();
            flame.update(context);
        }

        assertThat(flame.isDestroyed()).isTrue();
    }
}
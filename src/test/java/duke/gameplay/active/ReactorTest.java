package duke.gameplay.active;

import duke.gameplay.Bolt;
import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.ReactorDestructionSequence;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ReactorTest {
    private Reactor reactor;
    private GameplayContext context;

    @BeforeEach
    void createReactor() {
        reactor = new Reactor(0, 0);
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldSpin() {
        reactor.update(context);

        verify(context.getSoundManager()).play(Sfx.REACTOR_SOUND);
    }

    @Test
    void shouldInstakill() {
        assertThat(reactor.getDamage()).isGreaterThan(8);
    }

    @Test
    void shouldSpawnEffectsOnShot() {
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        GameplayContext context = GameplayContextFixture.create();

        reactor.onShot(context, bolt);

        verify(context.getActiveManager(), times(2)).spawn(anyList());
    }

    @Test
    void shouldBeDestroyed() {
        for (int i = 0; i < 10; i++) {
            assertThat(reactor.isDestroyed()).isFalse();
            reactor.onShot(context, mock());
        }

        assertThat(reactor.isDestroyed()).isTrue();
        verify(context.getActiveManager()).spawn(isA(ReactorDestructionSequence.class));
    }
}
package duke.gameplay.effects;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

class KillerBunnySpinTest {
    @Test
    void shouldAwardPointsAndSpawnSmokeWhenDestroyed() {
        KillerBunnySpin spin = new KillerBunnySpin(0, 0);
        GameplayContext context = GameplayContextFixture.create();

        for (int i = 0; i < KillerBunnySpin.TTL; i++) {
            spin.update(context);
        }

        assertThat(spin.isDestroyed()).isTrue();
        verify(context.getScoreManager()).score(5000, 0, 0);
        verify(context.getActiveManager()).spawn(anyList());
    }
}
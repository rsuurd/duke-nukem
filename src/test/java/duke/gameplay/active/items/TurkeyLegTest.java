package duke.gameplay.active.items;

import duke.gameplay.Bolt;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TurkeyLegTest {
    @Test
    void shouldBeShot() {
        GameplayContext context = GameplayContextFixture.create();
        Bolt bolt = mock();

        TurkeyLeg item = new TurkeyLeg(0, 0, mock(), mock());
        item.onShot(context, bolt);

        assertThat(item.isDestroyed()).isTrue();
        verify(context.getActiveManager()).spawn(isA(Item.class));
    }
}
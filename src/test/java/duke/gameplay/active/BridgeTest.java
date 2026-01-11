package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.Effect;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BridgeTest {
    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldDestroyWhenTouchingTwice() {
        Bridge bridge = new Bridge(160, 160, 64);

        updateWithPlayerAt(bridge, 160, 128); // 1st touch
        updateWithPlayerAt(bridge, 160, 100); // jump off
        updateWithPlayerAt(bridge, 160, 128); // 2nd touch

        assertThat(bridge.isDestroyed()).isTrue();
        verify(context.getActiveManager(), times(2)).spawn(any(Effect.class));
        verify(context.getActiveManager(), times(2)).spawn(anyList());
        verify(context.getSoundManager()).play(Sfx.BOX_EXPLODE);
    }

    private void updateWithPlayerAt(Bridge bridge, int playerX, int playerY) {
        when(context.getPlayer().getWidth()).thenReturn(16);
        when(context.getPlayer().getHeight()).thenReturn(32);
        when(context.getPlayer().getX()).thenReturn(playerX);
        when(context.getPlayer().getY()).thenReturn(playerY);

        bridge.update(context);
    }

}
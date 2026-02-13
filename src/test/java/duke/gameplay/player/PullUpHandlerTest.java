package duke.gameplay.player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PullUpHandlerTest {
    private PullUpHandler handler = new PullUpHandler();

    @Mock
    private Player player;

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"PULLING_UP"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldNotUpdateWhenNotPullingUp(State state) {
        when(player.getState()).thenReturn(state);

        handler.update(player);

        verify(player, never()).setY(anyInt());
        verify(player, never()).pullUpComplete();
    }

    @Test
    void shouldUpdateWhenPullingUp() {
        when(player.getState()).thenReturn(State.PULLING_UP);

        for (int offset : PullUpHandler.PULL_UP_OFFSETS) {
            handler.update(player);

            verify(player).setY(-offset);
        }

        verify(player).pullUpComplete();
    }
}

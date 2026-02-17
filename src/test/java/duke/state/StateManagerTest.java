package duke.state;

import duke.GameSystems;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StateManagerTest {
    @Mock
    private GameState current;

    @Mock
    private GameSystems context;

    @InjectMocks
    private StateManager stateManager;

    @Test
    void shouldStartInitialState() {
        verify(current).start(context);
    }

    @Test
    void shouldStopCurrentAndStartNewState() {
        GameState state = mock(GameState.class);
        stateManager.set(state);

        verify(current).stop(context);
        verify(state).start(context);
    }

    @Test
    void shouldUpdate() {
        stateManager.update();

        verify(current).update(context);
    }

    @Test
    void shouldRender() {
        stateManager.render();

        verify(current).render(context);
    }
}
package duke;

import duke.state.GameState;
import duke.state.StateManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameSystemsTest {
    @Mock
    private StateManager stateManager;

    @InjectMocks
    private GameSystems systems;

    @Test
    void shouldRequestState() {
        GameState gameState = mock();

        systems.requestState(gameState);

        verify(stateManager).set(gameState, systems);
    }
}

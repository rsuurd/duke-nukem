package duke.state;

import duke.GameSystems;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StateManagerTest {
    @Mock
    private GameState current;

    @Mock
    private GameSystems systems;

    @Test
    void shouldStopCurrentAndStartNewState() {
        GameState state = mock();

        StateManager manager = new StateManager(current);

        assertThat(manager.get()).isSameAs(current);

        manager.set(state, systems);

        verify(current).stop(systems);
        verify(state).start(systems);
        assertThat(manager.get()).isSameAs(state);
    }

    @Test
    void shouldStartState() {
        StateManager manager = new StateManager();

        manager.set(current, systems);

        verify(current).start(systems);
        assertThat(manager.get()).isSameAs(current);
    }
}
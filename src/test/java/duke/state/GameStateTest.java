package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import org.junit.jupiter.api.Test;

class GameStateTest {
    @Test
    void shouldRunDefaultLifeCycleMethods() {
        GameState state = new GameState() {
        };

        GameSystems systems = GameSystemsFixture.create();

        state.start(systems);
        state.update(systems);
        state.render(systems);
        state.stop(systems);
    }
}

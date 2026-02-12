package duke.state;

import duke.GameContext;
import duke.GameContextFixture;
import org.junit.jupiter.api.Test;

class GameStateTest {
    @Test
    void shouldRunDefaultLifeCycleMethods() {
        GameState state = new GameState() {
        };

        GameContext context = GameContextFixture.create();

        state.start(context);
        state.update(context);
        state.render(context);
        state.stop(context);
    }
}

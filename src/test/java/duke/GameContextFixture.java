package duke;

import static org.mockito.Mockito.mock;

public class GameContextFixture {
    public static GameContext create() {
        return new GameContext(mock(), mock(), mock(), mock(), mock(), mock());
    }
}

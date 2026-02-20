package duke;

import static org.mockito.Mockito.mock;

public class GameSystemsFixture {
    public static GameSystems create() {
        return new GameSystems(mock(), mock(), mock(), mock(), mock(), mock(), mock(), mock());
    }
}

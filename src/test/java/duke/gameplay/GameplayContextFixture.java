package duke.gameplay;

import static org.mockito.Mockito.mock;

public class GameplayContextFixture {
    public static GameplayContext create() {
        return new GameplayContext(mock(), mock(), mock(), mock(), mock(), mock(), mock());
    }
}

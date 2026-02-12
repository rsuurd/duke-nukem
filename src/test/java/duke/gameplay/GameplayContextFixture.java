package duke.gameplay;

import duke.level.Flags;

import static org.mockito.Mockito.mock;

public class GameplayContextFixture {
    public static GameplayContext create() {
        return new GameplayContext(mock(), mock(), mock(), mock(), mock(), mock(), mock(), mock(), mock());
    }

    public static final int SOLID_TILE_FLAG = Flags.SOLID.bit();
}

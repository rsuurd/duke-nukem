package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class EndTest {
    private End end;

    private GameSystems systems;

    @BeforeEach
    void create() {
        end = new End();

        systems = GameSystemsFixture.create();
    }

    @Test
    void shouldShowEndingBackground() {
        end.start(systems);

        verify(systems.getAssets()).getImage("END");
    }
}
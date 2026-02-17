package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.gfx.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreditsTest {
    private GameSystems systems;

    @BeforeEach
    void createContext() {
        systems = GameSystemsFixture.create();
    }

    @Test
    void shouldShowCredits() {
        Credits credits = new Credits();

        Sprite image = mock();
        when(systems.getAssets().getImage(any())).thenReturn(image);

        credits.start(systems);
        credits.render(systems);

        verify(systems.getAssets()).getImage("CREDITS");
        verify(systems.getRenderer()).draw(image, 0, 0);
    }
}
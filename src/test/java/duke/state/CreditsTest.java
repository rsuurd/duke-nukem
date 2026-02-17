package duke.state;

import duke.GameContext;
import duke.GameContextFixture;
import duke.gfx.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreditsTest {
    private GameContext context;

    @BeforeEach
    void createContext() {
        context = GameContextFixture.create();
    }

    @Test
    void shouldShowCredits() {
        Credits credits = new Credits();

        Sprite image = mock();
        when(context.getAssets().getImage(any())).thenReturn(image);

        credits.start(context);
        credits.render(context);

        verify(context.getAssets()).getImage("CREDITS");
        verify(context.getRenderer()).draw(image, 0, 0);
    }
}
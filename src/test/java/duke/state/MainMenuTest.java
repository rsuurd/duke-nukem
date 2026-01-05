package duke.state;

import duke.GameContext;
import duke.GameContextFixture;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class MainMenuTest {
    private MainMenu mainMenu = new MainMenu();

    @Test
    void shouldRender() {
        GameContext context = GameContextFixture.create();

        mainMenu.render(context);

        verify(context.getAssets()).getImage("DN");
        verify(context.getRenderer()).draw(any(), eq(0), eq(0));
    }
}

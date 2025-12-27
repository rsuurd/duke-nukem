package duke.state;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class MainMenuTest extends GameContextTestSupport {
    private MainMenu mainMenu = new MainMenu();

    @Test
    void shouldRender() {
        mainMenu.render(gameContext);

        verify(assets).getImage("DN");
        verify(renderer).draw(any(), eq(0), eq(0));
    }
}

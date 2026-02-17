package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class MainMenuTest {
    private MainMenu mainMenu = new MainMenu();

    @Test
    void shouldRender() {
        GameSystems systems = GameSystemsFixture.create();

        mainMenu.start(systems);
        mainMenu.render(systems);

        verify(systems.getAssets()).getImage("DN");
        verify(systems.getRenderer()).draw(any(), eq(0), eq(0));
    }
}

package duke.state;

import duke.GameContext;
import duke.Renderer;
import duke.resources.AssetManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MainMenuTest {
    @Mock
    private AssetManager assets;

    @Mock
    private Renderer renderer;

    @InjectMocks
    private GameContext context;

    private MainMenu mainMenu = new MainMenu();

    @Test
    void shouldRender() {
        mainMenu.render(context);

        verify(assets).getImage("DN");
        verify(renderer).draw(any(), eq(0), eq(0));
    }
}

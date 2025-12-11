package duke.state;

import duke.GameContext;
import duke.Renderer;
import duke.resources.ResourceLoader;
import duke.resources.SpriteLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainMenuTest {
    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private SpriteLoader spriteLoader;

    @Mock
    private Renderer renderer;

    @InjectMocks
    private GameContext context;

    private MainMenu mainMenu = new MainMenu();

    @Test
    void shouldStart() {
        when(resourceLoader.getSpriteLoader()).thenReturn(spriteLoader);

        mainMenu.start(context);

        verify(spriteLoader).readImage("DN.DN1");
    }

    @Test
    void shouldRender() {
        mainMenu.render(context);

        verify(renderer).draw(any(), eq(0), eq(0));
    }
}

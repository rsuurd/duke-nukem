package duke.dialog;

import duke.GameContext;
import duke.GameContextFixture;
import duke.Renderer;
import duke.gfx.Font;
import duke.gfx.Sprite;
import duke.resources.AssetManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.awt.event.KeyEvent.VK_ENTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DialogManagerTest {
    @Mock
    private AssetManager assetManager;

    @Mock
    private Font font;

    @InjectMocks
    private DialogManager dialogManager;

    @Mock
    private Renderer renderer;

    @BeforeEach
    void setupAssets() {
        List<Sprite> sprites = mock();
        lenient().when(sprites.get(anyInt())).thenReturn(mock());
        lenient().when(assetManager.getBorder()).thenReturn(sprites);
        lenient().when(assetManager.getObjects()).thenReturn(sprites);
    }

    @Test
    void shouldDoNothingWithoutActiveDialog() {
        assertThat(dialogManager.hasDialog()).isFalse();

        dialogManager.render(renderer);

        verifyNoInteractions(renderer);
    }

    @Test
    void shouldOpenDialog() {
        dialogManager.open(Dialog.create("Hello, World!", true));

        assertThat(dialogManager.hasDialog()).isTrue();

        dialogManager.render(renderer);

        verify(renderer, atLeastOnce()).draw(any(), anyInt(), anyInt());
    }

    @Test
    void shouldCloseDialogWithCursorWithEnter() {
        GameContext gameContext = GameContextFixture.create();

        dialogManager.open(Dialog.create("Press ENTER:", true));
        when(gameContext.getKeyHandler().isPressed(VK_ENTER)).thenReturn(true);

        dialogManager.update(gameContext);

        assertThat(dialogManager.hasDialog()).isFalse();
    }

    @Test
    void shouldCloseDialog() {
        dialogManager.open(Dialog.create("Hello, World!", true));

        assertThat(dialogManager.hasDialog()).isTrue();

        dialogManager.close();

        assertThat(dialogManager.hasDialog()).isFalse();
    }
}
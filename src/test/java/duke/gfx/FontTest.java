package duke.gfx;

import duke.Renderer;
import duke.resources.AssetManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FontTest {
    @Mock
    private List<Sprite> fonts;

    @Mock
    private AssetManager assets;

    @InjectMocks
    private Font font;

    @Mock
    private Renderer renderer;

    @BeforeEach
    void setUp() {
        when(assets.getFont()).thenReturn(fonts);
    }

    @Test
    void shouldDrawText() {
        font.drawText(renderer, "Hello\nWorld", 0, 0);

        verify(renderer, times(10)).draw(any(), anyInt(), anyInt());
    }
}
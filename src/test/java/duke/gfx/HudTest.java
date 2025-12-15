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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HudTest {
    @Mock
    private List<Sprite> borders;

    @Mock
    private List<Sprite> objects;

    @Mock
    private AssetManager assets;

    @Mock
    private Font font;

    @InjectMocks
    private Hud hud;

    @Mock
    private Renderer renderer;

    @BeforeEach
    void create() {
        when(assets.getBorder()).thenReturn(borders);
        when(assets.getObjects()).thenReturn(objects);
    }

    @Test
    void shouldDrawScore() {
        hud.render(renderer);

        verify(font).drawText(renderer, "00000000", 240, 24);
    }
}
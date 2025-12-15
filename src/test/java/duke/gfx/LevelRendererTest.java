package duke.gfx;

import duke.Renderer;
import duke.level.Level;
import duke.resources.AssetManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static duke.level.Level.TILE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LevelRendererTest {
    @Mock
    private Level level;

    @Mock
    private Renderer renderer;

    @Mock
    private AssetManager assets;

    @Mock
    private Viewport viewport;

    @InjectMocks
    private LevelRenderer levelRenderer;

    @Test
    void shouldDrawBackdrop() {
        Sprite backdrop = new Sprite(0, 0);
        when(assets.getBackdrop(level.getBackdrop())).thenReturn(backdrop);

        levelRenderer.render(renderer, viewport);

        verify(renderer).draw(backdrop, TILE_SIZE, TILE_SIZE);
    }

    @Test
    void shouldDrawTiles() {
        Sprite tile = new Sprite(0, 0);
        when(level.getTile(anyInt(), anyInt())).thenReturn(32);
        when(assets.getTiles()).thenReturn(List.of(tile, tile));

        levelRenderer.render(renderer, viewport);

        verify(renderer, times(180)).draw(eq(tile), anyInt(), anyInt());
    }

    @Test
    void shouldTickFlasher() {
        for (int flasher = 1; flasher <= 3; flasher++) {
            levelRenderer.render(renderer, viewport);
            assertThat(levelRenderer.flasher).isEqualTo(flasher);
        }

        levelRenderer.render(renderer, viewport);
        assertThat(levelRenderer.flasher).isEqualTo(0);
    }
}

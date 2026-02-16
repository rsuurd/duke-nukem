package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.Layer;
import duke.gfx.SpriteRenderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaterTest {
    @Mock
    private Renderer renderer;

    @Mock
    private SpriteRenderer spriteRenderer;

    @Test
    void shouldRenderReflections() {
        Water water = new Water(0, 0);

        water.render(renderer, spriteRenderer, 32, 64);

        verify(renderer, atLeastOnce()).copy(anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    void shouldNotRenderOffScreen() {
        Water water = new Water(0, 0);

        water.render(renderer, spriteRenderer, 0, 0);

        verify(renderer, never()).copy(anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    void shouldBePostProcessLayer() {
        Water water = new Water(0, 0);

        assertThat(water.getLayer()).isEqualTo(Layer.POST_PROCESS);
    }
}

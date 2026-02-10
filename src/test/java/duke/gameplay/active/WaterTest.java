package duke.gameplay.active;

import duke.Renderer;
import duke.gfx.Sprite;
import duke.gfx.SpriteRenderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WaterTest {
    @Mock
    private Renderer renderer;

    @Mock
    private SpriteRenderer spriteRenderer;

    @Test
    void shouldRenderReflections() {
        Water water = new Water(0, 0);

        water.render(renderer, spriteRenderer, 0, 0);

        verify(renderer).draw(any(Sprite.class), eq(0), eq(0));
    }
}

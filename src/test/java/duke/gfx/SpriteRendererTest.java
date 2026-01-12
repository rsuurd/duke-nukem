package duke.gfx;

import duke.Renderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpriteRendererTest {
    @Mock
    private Renderer renderer;

    @InjectMocks
    private SpriteRenderer spriteRenderer;

    @Test
    void shouldRenderQuadSprite() {
        Sprite sprite = new Sprite(16, 16);
        SpriteDescriptor spriteDescriptor = new SpriteDescriptor(assets -> List.of(sprite, sprite, sprite, sprite), 0, -8, -8, 2, 2);

        SpriteRenderable renderable = mock();
        when(renderable.isVisible()).thenReturn(true);
        when(renderable.getSpriteDescriptor()).thenReturn(spriteDescriptor);

        spriteRenderer.render(renderer, renderable, 128, 128);

        verify(renderer).draw(sprite, 120, 120);
        verify(renderer).draw(sprite, 136, 120);
        verify(renderer).draw(sprite, 120, 136);
        verify(renderer).draw(sprite, 136, 136);
    }

    @Test
    void shouldSkipInvisibleRenderables() {
        SpriteRenderable renderable = mock();
        when(renderable.isVisible()).thenReturn(false);

        spriteRenderer.render(renderer, renderable, 128, 128);

        verify(renderable, never()).getSpriteDescriptor();
        verifyNoInteractions(renderer);
    }
}

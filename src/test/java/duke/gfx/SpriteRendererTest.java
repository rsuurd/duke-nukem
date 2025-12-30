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
        when(renderable.getSpriteDescriptor()).thenReturn(spriteDescriptor);
        when(renderable.getBaseIndex()).thenReturn(0);

        spriteRenderer.render(renderer, renderable, 0, 0);

        verify(renderer).draw(sprite, -8, -8);
        verify(renderer).draw(sprite, 8, -8);
        verify(renderer).draw(sprite, -8, 8);
        verify(renderer).draw(sprite, 8, 8);
    }
}

package duke.gfx;

import duke.Renderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnimationRendererTest {
    @Mock
    private Renderer renderer;

    @InjectMocks
    private AnimationRenderer animationRenderer;

    @Test
    void shouldRenderAnimation() {
        Sprite sprite = new Sprite(16, 16);
        SpriteDescriptor spriteDescriptor = new SpriteDescriptor(assets -> List.of(sprite, sprite, sprite, sprite), 0, -8, -8, 2, 2);
        AnimationDescriptor animationDescriptor = new AnimationDescriptor(spriteDescriptor, 1, 1);
        Animation animation = new Animation(animationDescriptor);

        animationRenderer.render(renderer, animation, 0, 0);

        verify(renderer).draw(sprite, -8, -8);
        verify(renderer).draw(sprite, 8, -8);
        verify(renderer).draw(sprite, -8, 8);
        verify(renderer).draw(sprite, 8, 8);
    }
}

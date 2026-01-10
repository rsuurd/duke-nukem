package duke.gfx;

import duke.gameplay.Layer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnimatedSpriteRenderableTest {
    @Mock
    private Animation animation;

    @Test
    void shouldTickAnimation() {
        new AnimatedSpriteRenderable(animation, Layer.FOREGROUND).tick();

        verify(animation).tick();
    }

    @Test
    void shouldSetAnimation() {
        AnimationDescriptor descriptor = mock();

        new AnimatedSpriteRenderable(animation, Layer.FOREGROUND).setAnimation(descriptor);

        verify(animation).setAnimation(descriptor);
    }
}

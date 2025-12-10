package duke.gfx;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpriteTest {
    @Test
    void shouldValidateDimensions() {
        assertThatThrownBy(() ->
                new Sprite(32, 32, new int[]{0})
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Sprite pixels do not match given dimensions");
    }

    @Test
    void shouldCreateBlank() {
        Sprite sprite = new Sprite(1, 1);

        assertThat(sprite.getPixel(0, 0)).isEqualTo(0);
    }

    @Test
    void shouldClear() {
        Sprite sprite = new Sprite(1, 1, new int[]{1});

        sprite.clear();

        assertThat(sprite.getPixel(0, 0)).isEqualTo(0);
    }

    @Test
    void shouldRetrievePixels() {
        Sprite sprite = new Sprite(1, 1, new int[]{3});

        assertThat(sprite.getWidth()).isEqualTo(1);
        assertThat(sprite.getHeight()).isEqualTo(1);
        assertThat(sprite.getPixel(0, 0)).isEqualTo(3);
    }

    @Test
    void shouldOverlay() {
        Sprite sprite = new Sprite(1, 1, new int[]{1});
        Sprite overlay = new Sprite(1, 1, new int[]{3});

        sprite.draw(overlay, 0, 0);

        assertThat(sprite.getWidth()).isEqualTo(1);
        assertThat(sprite.getHeight()).isEqualTo(1);
        assertThat(sprite.getPixel(0, 0)).isEqualTo(3);
    }
}

package duke.gameplay;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RectangleTest {
    @Test
    void shouldIntersect() {
        Rectangle rectangle = new Rectangle(0, 0, 8, 8);

        assertThat(rectangle.intersects(rectangle)).isTrue();
        assertThat(rectangle.intersects(new Rectangle(4, 4, 8, 8))).isTrue();
        assertThat(rectangle.intersects(new Rectangle(16, 16, 8, 8))).isFalse();
    }
}

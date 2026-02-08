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

    @Test
    void shouldChange() {
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        rectangle.setX(16);
        rectangle.setY(16);
        rectangle.setWidth(16);
        rectangle.setHeight(16);

        assertThat(rectangle.getX()).isEqualTo(16);
        assertThat(rectangle.getY()).isEqualTo(16);
        assertThat(rectangle.getWidth()).isEqualTo(16);
        assertThat(rectangle.getHeight()).isEqualTo(16);
    }
}

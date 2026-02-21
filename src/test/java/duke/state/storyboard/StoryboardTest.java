package duke.state.storyboard;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class StoryboardTest {
    @Test
    void shouldGoThroughStoryboard() {
        Panel first = mock();
        Panel second = mock();
        Panel third = mock();

        Storyboard storyboard = new Storyboard(List.of(first, second, third));

        assertThat(storyboard.current()).isSameAs(first);
        assertThat(storyboard.hasNext()).isTrue();

        storyboard.advance();
        assertThat(storyboard.current()).isSameAs(second);
        assertThat(storyboard.hasNext()).isTrue();

        storyboard.advance();
        assertThat(storyboard.current()).isSameAs(third);
        assertThat(storyboard.hasNext()).isFalse();
    }
}

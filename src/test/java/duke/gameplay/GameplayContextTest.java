package duke.gameplay;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class GameplayContextTest {
    @Test
    void shouldAddActive() {
        GameplayContext context = new GameplayContext(mock(), mock());

        Active active = mock();

        context.spawn(active);

        assertThat(context.getActives()).containsExactly(active);
    }
}

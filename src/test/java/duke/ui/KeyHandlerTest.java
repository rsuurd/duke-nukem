package duke.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.event.KeyEvent;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KeyHandlerTest {
    private KeyHandler handler = new KeyHandler();

    @ParameterizedTest
    @MethodSource("keys")
    void shouldHandle(int keyCode, Function<KeyHandler, Boolean> input) {
        KeyEvent keyEvent = create(keyCode);

        handler.keyPressed(keyEvent);
        assertThat(input.apply(handler)).isTrue();

        handler.keyReleased(keyEvent);
        assertThat(input.apply(handler)).isFalse();
    }

    @Test
    void shouldClear() {
        handler.keyPressed(create(KeyEvent.VK_SHIFT));
        handler.clear();

        assertThat(handler.isAnyKeyPressed()).isFalse();
    }

    static Stream<Arguments> keys() {
        return Stream.of(
                Arguments.of(KeyEvent.VK_UP, (Function<KeyHandler, Boolean>) handler -> handler.getInput().up()),
                Arguments.of(KeyEvent.VK_DOWN, (Function<KeyHandler, Boolean>) handler -> handler.getInput().down()),
                Arguments.of(KeyEvent.VK_LEFT, (Function<KeyHandler, Boolean>) handler -> handler.getInput().left()),
                Arguments.of(KeyEvent.VK_RIGHT, (Function<KeyHandler, Boolean>) handler -> handler.getInput().right()),
                Arguments.of(KeyEvent.VK_ALT, (Function<KeyHandler, Boolean>) handler -> handler.getInput().fire()),
                Arguments.of(KeyEvent.VK_CONTROL, (Function<KeyHandler, Boolean>) handler -> handler.getInput().jump()),
                Arguments.of(KeyEvent.VK_SPACE, (Function<KeyHandler, Boolean>) KeyHandler::isAnyKeyPressed)
        );
    }

    private KeyEvent create(int keyCode) {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(keyCode);

        return event;
    }
}

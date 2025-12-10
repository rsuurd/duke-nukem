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
    void shouldHandle(int keyCode, Function<KeyHandler, Boolean> expected) {
        KeyEvent keyEvent = create(keyCode);

        handler.keyPressed(keyEvent);
        assertThat(expected.apply(handler)).isTrue();

        handler.keyReleased(keyEvent);
        assertThat(expected.apply(handler)).isFalse();
    }

    @Test
    void shouldClear() {
        handler.keyPressed(create(KeyEvent.VK_SHIFT));
        handler.clear();

        assertThat(handler.isAnyKeyPressed()).isFalse();
    }

    static Stream<Arguments> keys() {
        return Stream.of(
                Arguments.of(KeyEvent.VK_LEFT, (Function<KeyHandler, Boolean>) KeyHandler::isLeft),
                Arguments.of(KeyEvent.VK_RIGHT, (Function<KeyHandler, Boolean>) KeyHandler::isRight),
                Arguments.of(KeyEvent.VK_UP, (Function<KeyHandler, Boolean>) KeyHandler::isUsing),
                Arguments.of(KeyEvent.VK_ALT, (Function<KeyHandler, Boolean>) KeyHandler::isJump),
                Arguments.of(KeyEvent.VK_CONTROL, (Function<KeyHandler, Boolean>) KeyHandler::isFire),
                Arguments.of(KeyEvent.VK_SPACE, (Function<KeyHandler, Boolean>) KeyHandler::isAnyKeyPressed)
        );
    }

    private KeyEvent create(int keyCode) {
        KeyEvent event = mock(KeyEvent.class);
        when(event.getKeyCode()).thenReturn(keyCode);

        return event;
    }
}

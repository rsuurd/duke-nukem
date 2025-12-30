package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.Player;
import duke.level.Level;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityCameraTest {
    @Mock
    private Player player;
    @Mock
    private Level level;

    @InjectMocks
    private GameplayContext context;

    @ParameterizedTest
    @MethodSource("playerLocation")
    void shouldFacePlayer(int playerX, int expectedBaseIndex) {
        when(context.getPlayer().getX()).thenReturn(playerX);

        SecurityCamera securityCamera = new SecurityCamera(16, 0);

        securityCamera.update(context);

        assertThat(securityCamera.getBaseIndex()).isEqualTo(expectedBaseIndex);
    }

    static Stream<Arguments> playerLocation() {
        return Stream.of(
                () -> new Object[]{0, 208},
                () -> new Object[]{16, 209},
                () -> new Object[]{32, 210}
        );
    }
}
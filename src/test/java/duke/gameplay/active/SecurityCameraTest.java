package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.Effect;
import duke.sfx.Sfx;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityCameraTest {
    @ParameterizedTest
    @MethodSource("playerLocation")
    void shouldFacePlayer(int playerX, int expectedBaseIndex) {
        GameplayContext context = GameplayContextFixture.create();
        when(context.getPlayer().getX()).thenReturn(playerX);

        SecurityCamera securityCamera = new SecurityCamera(16, 0);

        securityCamera.update(context);

        assertThat(securityCamera.getSpriteDescriptor().baseIndex()).isEqualTo(expectedBaseIndex);
    }

    @Test
    void shouldBeShot() {
        GameplayContext context = GameplayContextFixture.create();
        SecurityCamera securityCamera = new SecurityCamera(16, 0);

        securityCamera.onShot(context, mock());

        verify(context.getActiveManager()).spawn(any(Effect.class));
        assertThat(securityCamera.isActive()).isFalse();
        verify(context.getScoreManager()).score(100, 16, 0);
        verify(context.getSoundManager()).play(Sfx.SMALL_DEATH);
    }

    static Stream<Arguments> playerLocation() {
        return Stream.of(
                () -> new Object[]{0, 208},
                () -> new Object[]{16, 209},
                () -> new Object[]{32, 210}
        );
    }
}
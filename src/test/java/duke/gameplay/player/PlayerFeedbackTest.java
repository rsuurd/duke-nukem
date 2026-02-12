package duke.gameplay.player;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.Effect;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerFeedbackTest {
    private PlayerFeedback feedback;

    @Mock
    private Player player;
    private GameplayContext context;

    @BeforeEach
    void create() {
        feedback = new PlayerFeedback();
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldPlaySoundWhenJumped() {
        feedback.provideFeedback(context, player, true, false, false, false);

        verify(context.getSoundManager()).play(Sfx.PLAYER_JUMP);
    }

    @Test
    void shouldPlaySoundWhenBumped() {
        feedback.provideFeedback(context, player, false, true, false, false);

        verify(context.getSoundManager()).play(Sfx.HIT_HEAD);
    }

    @Test
    void shouldPlaySoundAndSpawnDustWhenLanded() {
        feedback.provideFeedback(context, player, false, false, true, false);

        verify(context.getSoundManager()).play(Sfx.PLAYER_LAND);
        verify(context.getActiveManager()).spawn(isA(Effect.class));
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"WALKING", "CLINGING"})
    void shouldPlaySoundEveryOtherFrameWhile(State state) {
        when(player.isMoving()).thenReturn(true);
        when(player.getState()).thenReturn(state);

        for (int i = 0; i < 4; i++) {
            feedback.provideFeedback(context, player, false, false, false, false);
        }

        Sfx expectedSfx = switch (state) {
            case WALKING -> Sfx.WALKING;
            case CLINGING -> Sfx.CLING_HOOKS;
            default -> fail("Unexpected state: " + state);
        };

        verify(context.getSoundManager(), times(2)).play(expectedSfx);
    }

    @Test
    void shouldPlaySoundWhenDamageTaken() {
        feedback.provideFeedback(context, player, false, false, false, true);

        verify(context.getSoundManager()).play(Sfx.PLAYER_HIT);
    }
}
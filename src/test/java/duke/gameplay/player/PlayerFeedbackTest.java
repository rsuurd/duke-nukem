package duke.gameplay.player;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.Effect;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(player.getState()).thenReturn(State.STANDING);
    }

    @Test
    void shouldPlaySoundWhenJumped() {
        feedback.provideFeedback(context, player, true, false, false);

        verify(context.getSoundManager()).play(Sfx.PLAYER_JUMP);
    }

    @Test
    void shouldPlaySoundWhenBumped() {
        feedback.provideFeedback(context, player, false, true, false);

        verify(context.getSoundManager()).play(Sfx.HIT_HEAD);
    }

    @Test
    void shouldPlaySoundAndSpawnDustWhenLanded() {
        feedback.provideFeedback(context, player, false, false, true);

        verify(context.getSoundManager()).play(Sfx.PLAYER_LAND);
        verify(context.getActiveManager()).spawn(isA(Effect.class));
    }

    @Test
    void shouldPlaySoundEveryOtherFrameWhileWalking() {
        when(player.getState()).thenReturn(State.WALKING);

        for (int i = 0; i < 4; i++) {
            feedback.provideFeedback(context, player, false, false, false);
        }

        verify(context.getSoundManager(), times(2)).play(Sfx.WALKING);
    }
}
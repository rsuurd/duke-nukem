package duke.gameplay.active.items;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CharacterBehaviorTest {
    private CharacterBehavior behavior;
    private GameplayContext context;

    @BeforeEach
    void createBehavior() {
        behavior = new CharacterBehavior('D');
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldKeepTrack() {
        behavior.pickedUp(context, mock());

        verify(context.getBonusTracker()).trackDUKE('D');
        verify(context.getScoreManager()).score(500, 0, 0);
        verify(context.getSoundManager()).play(Sfx.GET_BONUS_OBJECT);
    }

    @Test
    void shouldRewardIfComplete() {
        when(context.getBonusTracker().trackDUKE(anyChar())).thenReturn(true);

        behavior.pickedUp(context, mock());

        verify(context.getScoreManager()).score(10000, 0, 0);
        verify(context.getSoundManager()).play(Sfx.GET_DUKE_SOUND);
    }
}
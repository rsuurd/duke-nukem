package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.sfx.Sfx;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SoundBehaviorTest {
    @Test
    void shouldPlaySfx() {
        GameplayContext context = GameplayContextFixture.create();

        new SoundBehavior(Sfx.GET_BONUS_OBJECT).pickedUp(context, mock());

        verify(context.getSoundManager()).play(Sfx.GET_BONUS_OBJECT);
    }
}

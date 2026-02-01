package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.Explosion;
import duke.sfx.Sfx;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ExplodingItemBehaviorTest {
    @Test
    void shouldSpawnExplosion() {
        GameplayContext context = GameplayContextFixture.create();

        new ExplodingItemBehavior().pickedUp(context, mock());

        verify(context.getActiveManager()).spawn(isA(Explosion.class));
        verify(context.getSoundManager()).play(Sfx.BOMB_EXPLODE);
    }
}
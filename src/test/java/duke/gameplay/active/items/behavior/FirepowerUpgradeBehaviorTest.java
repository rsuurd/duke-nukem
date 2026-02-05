package duke.gameplay.active.items.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.player.Weapon;
import duke.sfx.Sfx;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class FirepowerUpgradeBehaviorTest {
    @Test
    void shouldIncreaseFirepower() {
        GameplayContext context = GameplayContextFixture.create();
        Weapon weapon = mock();
        when(context.getPlayer().getWeapon()).thenReturn(weapon);

        new FirepowerUpgradeBehavior().pickedUp(context, mock());

        verify(context.getPlayer().getWeapon()).increaseFirepower();
        verify(context.getSoundManager()).play(Sfx.SPECIAL_ITEM);
        verify(context.getScoreManager()).score(1000, 0, 0);
    }
}

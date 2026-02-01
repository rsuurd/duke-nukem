package duke.gameplay.active.items.behavior;

import duke.dialog.Hints;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SecretTipBehaviorTest {
    @ParameterizedTest
    @EnumSource(value = Hints.Hint.class, names = {"NUCLEAR_MOLECULE", "SODA", "TURKEY"})
    void shouldShowHint(Hints.Hint type) {
        GameplayContext context = GameplayContextFixture.create();
        ItemBehavior behavior = new SecretTipBehavior(type);

        behavior.pickedUp(context, mock());

        verify(context.getHints()).showHint(type, context);
    }
}

package duke.gameplay.active;

import duke.dialog.Hints;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.player.Inventory;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AccessCardActivationPointTest {
    private AccessCardActivationPoint accessCardActivationPoint;

    private GameplayContext context;

    @BeforeEach
    void createAccessCardActivationPoint() {
        accessCardActivationPoint = new AccessCardActivationPoint(0, 0);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldStartWithForceFieldActive() {
        assertThat(accessCardActivationPoint.isForceFieldDeactivated()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldBeInteractable(boolean intersects) {
        when(context.getPlayer().intersects(accessCardActivationPoint)).thenReturn(intersects);

        assertThat(accessCardActivationPoint.canInteract(context.getPlayer())).isEqualTo(intersects);
    }

    @Test
    void shouldShowHint() {
        when(context.getPlayer().intersects(accessCardActivationPoint)).thenReturn(true);

        accessCardActivationPoint.update(context);

        verify(context.getHints()).showHint(Hints.Hint.ACCESS_CARD, context);
    }

    @Test
    void shouldDeactivateForceFieldWhenAccessCardIsUsed() {
        Inventory inventory = mock();
        when(inventory.removeEquipment(any())).thenReturn(true);
        when(context.getPlayer().getInventory()).thenReturn(inventory);

        ForceField forceField = mock();
        when(context.getActiveManager().getActives()).thenReturn(new LinkedList<>(List.of(forceField)));

        accessCardActivationPoint.interactRequested(context);

        assertThat(accessCardActivationPoint.isForceFieldDeactivated()).isTrue();
        verify(context.getSoundManager()).play(Sfx.OPEN_KEY_DOOR);
        assertThat(context.getActiveManager().getActives()).doesNotHaveAnyElementsOfTypes(ForceField.class);

        assertThat(accessCardActivationPoint.canInteract(context.getPlayer())).isFalse();
    }

    @Test
    void shouldNotDeactivateForceFieldWithoutAccessCard() {
        Inventory inventory = mock();
        when(inventory.removeEquipment(any())).thenReturn(false);
        when(context.getPlayer().getInventory()).thenReturn(inventory);

        ForceField forceField = mock();
        when(context.getActiveManager().getActives()).thenReturn(new LinkedList<>(List.of(forceField)));

        accessCardActivationPoint.interactRequested(context);

        assertThat(accessCardActivationPoint.isForceFieldDeactivated()).isFalse();
        assertThat(context.getActiveManager().getActives()).hasExactlyElementsOfTypes(ForceField.class);
        verify(context.getSoundManager()).play(Sfx.CHEAT_MODE);
        verify(context.getDialogManager()).open(any());
    }
}
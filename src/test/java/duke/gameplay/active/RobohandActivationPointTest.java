package duke.gameplay.active;

import duke.dialog.Hints;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.player.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RobohandActivationPointTest {
    private RobohandActivationPoint robohandActivationPoint;

    private GameplayContext context;

    @BeforeEach
    void createAccessCardActivationPoint() {
        robohandActivationPoint = new RobohandActivationPoint(0, 0);

        context = GameplayContextFixture.create();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldBeInteractable(boolean intersects) {
        when(context.getPlayer().intersects(robohandActivationPoint)).thenReturn(intersects);

        assertThat(robohandActivationPoint.canInteract(context.getPlayer())).isEqualTo(intersects);
    }

    @Test
    void shouldShowHint() {
        when(context.getPlayer().intersects(robohandActivationPoint)).thenReturn(true);

        robohandActivationPoint.update(context);

        verify(context.getHints()).showHint(Hints.Hint.ROBOHAND, context);
    }

    @Test
    void shouldExtendBridgeWhenRobohandIsUsed() {
        Inventory inventory = mock();
        when(inventory.isEquippedWith(any())).thenReturn(true);
        when(context.getPlayer().getInventory()).thenReturn(inventory);

        Girder girder = mock();
        when(context.getActiveManager().getActives()).thenReturn(List.of(girder));

        robohandActivationPoint.interactRequested(context);

        verify(girder).extend(context);

        assertThat(robohandActivationPoint.canInteract(context.getPlayer())).isFalse();
    }

    @Test
    void shouldNotExtendBridgeWithoutAccessCard() {
        Inventory inventory = mock();
        when(inventory.isEquippedWith(any())).thenReturn(false);
        when(context.getPlayer().getInventory()).thenReturn(inventory);
        Girder girder = mock();
        when(context.getActiveManager().getActives()).thenReturn(List.of(girder));

        robohandActivationPoint.interactRequested(context);

        verifyNoInteractions(girder);
        verify(context.getDialogManager()).open(any());
    }
}

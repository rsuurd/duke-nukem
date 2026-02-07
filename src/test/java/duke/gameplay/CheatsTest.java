package duke.gameplay;

import duke.gameplay.active.items.Key;
import duke.gameplay.player.Inventory;
import duke.gameplay.player.PlayerHealth;
import duke.gameplay.player.Weapon;
import duke.ui.KeyHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.Cheats.GOD;
import static duke.gameplay.Cheats.GOW;
import static duke.gameplay.player.PlayerHealth.MAX_HEALTH;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheatsTest {
    @Mock
    private KeyHandler keyHandler;

    private GameplayContext context;

    @BeforeEach
    void create() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldNotCheatIfDisabled() {
        Cheats cheats = new Cheats(false);

        cheats.processInput(keyHandler, context);

        verifyNoInteractions(keyHandler);
    }

    @Test
    void shouldMaxOutFirePowerAndAddAllItems() {
        PlayerHealth health = mock();
        Weapon weapon = mock();
        Inventory inventory = mock();
        when(context.getPlayer().getHealth()).thenReturn(health);
        when(context.getPlayer().getWeapon()).thenReturn(weapon);
        when(context.getPlayer().getInventory()).thenReturn(inventory);

        Cheats cheats = createCheatsWithPressedKeys(GOD);

        cheats.processInput(keyHandler, context);

        verify(health).increaseHealth(MAX_HEALTH);
        verify(weapon, times(3)).increaseFirepower();

        for (Key.Type keyType : Key.Type.values()) {
            verify(inventory).addKey(keyType);
        }

        for (Inventory.Equipment equipment : Inventory.Equipment.values()) {
            verify(inventory).addEquipment(equipment);
        }
    }

    @Test
    void shouldCompleteLevel() {
        Cheats cheats = createCheatsWithPressedKeys(GOW);

        cheats.processInput(keyHandler, context);

        verify(context.getLevel()).complete();
    }

    @Test
    void shouldNotDoubleActivate() {
        Cheats cheats = createCheatsWithPressedKeys(GOW);

        cheats.processInput(keyHandler, context);
        cheats.processInput(keyHandler, context);

        verify(context.getLevel(), times(1)).complete();
    }

    private Cheats createCheatsWithPressedKeys(int[] keySequence) {
        Cheats cheats = new Cheats(true);

        when(keyHandler.isPressed(anyInt())).thenReturn(false);

        for (int key : keySequence) {
            when(keyHandler.isPressed(key)).thenReturn(true);
        }

        return cheats;
    }
}

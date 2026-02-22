package duke.gameplay;

import duke.gameplay.player.Inventory;
import duke.gameplay.player.PlayerHealth;
import duke.gameplay.player.Weapon;
import duke.level.LevelDescriptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SaveGameFactoryTest {
    @Test
    void shouldCreateSaveGame() {
        GameplayContext context = GameplayContextFixture.create();

        when(context.getPlayer().getHealth()).thenReturn(new PlayerHealth());
        when(context.getPlayer().getWeapon()).thenReturn(new Weapon());
        when(context.getPlayer().getInventory()).thenReturn(new Inventory());
        when(context.getLevel().getDescriptor()).thenReturn(new LevelDescriptor(1, 1, 0));

        SaveGameFactory factory = new SaveGameFactory();

        SaveGame saveGame = factory.create(context);

        assertThat(saveGame).isNotNull();
    }
}
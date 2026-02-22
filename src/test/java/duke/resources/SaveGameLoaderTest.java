package duke.resources;

import duke.gameplay.SaveGame;
import duke.gameplay.player.Inventory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveGameLoaderTest {
    @Mock
    private Path path;

    @InjectMocks
    private SaveGameLoader loader;

    @Test
    void shouldSaveAndLoadGame() throws IOException {
        when(path.resolve(anyString())).thenReturn(createTemp());

        SaveGame saveGame = new SaveGame(0, 0, 0, 0, 1, 8, 2, new Inventory(), Set.of(), 1000000);

        loader.save(saveGame, '1');

        verify(path).resolve("SAVED1.DN1");

        SaveGame restore = loader.load('1');

        assertThat(restore).usingRecursiveComparison().ignoringFields("inventory").isEqualTo(saveGame);

        for (Inventory.Equipment equipment : Inventory.Equipment.values()) {
            assertThat(restore.inventory().isEquippedWith(equipment)).isEqualTo(saveGame.inventory().isEquippedWith(equipment));
        }
    }

    private Path createTemp() throws IOException {
        Path temp = Files.createTempFile("saved", "dn1");

        temp.toFile().deleteOnExit();

        return temp;
    }
}

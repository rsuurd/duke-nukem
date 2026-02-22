package duke.gameplay;

import duke.dialog.Hints;
import duke.gameplay.player.Inventory;

import java.util.Set;

public record SaveGame(
        int cameraX, int cameraY,
        int playerX, int playerY,
        int level,
        int health,
        int firepower,
        Inventory inventory,
        Set<Hints.Hint> hints,
        int score
) {
}

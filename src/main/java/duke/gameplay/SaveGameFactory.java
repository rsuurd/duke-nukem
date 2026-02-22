package duke.gameplay;

import duke.dialog.Hints;
import duke.gameplay.player.Player;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SaveGameFactory {
    public SaveGame create(GameplayContext context) {
        Player player = context.getPlayer();

        int playerX = player.getX();
        int playerY = player.getY();
        int health = player.getHealth().getCurrent();
        int firepower = player.getWeapon().getFirepower();
        int level = context.getLevel().getDescriptor().index();
        int score = context.getScoreManager().getScore();

        return new SaveGame(0, 0, playerX, playerY, level, health, firepower,
                player.getInventory(), getAvailableHints(context.getHints()), score);
    }

    private Set<Hints.Hint> getAvailableHints(Hints hints) {
        return Stream.of(Hints.Hint.values()).filter(hints::isHintAvailable).collect(Collectors.toSet());
    }
}

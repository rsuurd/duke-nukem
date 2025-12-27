package duke.gameplay;

import duke.level.Level;

// actives, shots, score, etc?
public record GameplayContext(Player player, Level level) {}

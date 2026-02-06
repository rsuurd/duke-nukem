package duke.gameplay.player;

import duke.gameplay.GameplayContext;
import duke.gameplay.effects.EffectsFactory;
import duke.sfx.SoundManager;

import static duke.sfx.Sfx.*;

public class PlayerFeedback {
    private int timer;

    public void provideFeedback(GameplayContext context, Player player, boolean jumped, boolean bumped, boolean landed, boolean damageTaken) {
        SoundManager sounds = context.getSoundManager();

        if (jumped) {
            sounds.play(PLAYER_JUMP);
        }

        if (bumped) {
            sounds.play(HIT_HEAD);
        }

        if (landed) {
            context.getActiveManager().spawn(EffectsFactory.createDust(player));
            sounds.play(PLAYER_LAND);
        }

        if (damageTaken) {
            sounds.play(PLAYER_HIT);
        }

        if (player.getState() == State.WALKING) {
            if (timer == 0) {
                sounds.play(WALKING);
            }

            timer = (timer + 1) % 2;
        } else {
            timer = 0;
        }
    }
}

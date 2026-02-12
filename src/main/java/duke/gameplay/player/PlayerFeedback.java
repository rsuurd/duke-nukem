package duke.gameplay.player;

import duke.gameplay.GameplayContext;
import duke.gameplay.effects.EffectsFactory;
import duke.sfx.Sfx;
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

        if (player.isMoving()) {
            switch (player.getState()) {
                case WALKING -> playTimedSound(sounds, WALKING);
                case CLINGING -> playTimedSound(sounds, CLING_HOOKS);
                default -> timer = 0;
            }
        }
    }

    private void playTimedSound(SoundManager sounds, Sfx sfx) {
        if (timer == 0) {
            sounds.play(sfx);
        }

        timer = (timer + 1) % 2;
    }

}

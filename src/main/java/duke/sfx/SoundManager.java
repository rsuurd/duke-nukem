package duke.sfx;

import duke.resources.AssetManager;

public class SoundManager {
    private AssetManager assets;
    private PcSpeaker pcSpeaker;

    private boolean enabled;

    public SoundManager(AssetManager assets, PcSpeaker pcSpeaker) {
        this.assets = assets;
        this.pcSpeaker = pcSpeaker;

        enabled = true;
    }

    public void toggle() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void play(Sfx sfx) {
        Sound sound = assets.getSounds().get(sfx.ordinal());

        play(sound);
    }

    private void play(Sound sound) {
        if (enabled) {
            pcSpeaker.play(sound);
        }
    }
}

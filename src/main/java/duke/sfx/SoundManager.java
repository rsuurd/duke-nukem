package duke.sfx;

import duke.DukeNukemException;
import duke.resources.AssetManager;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SoundManager {
    public static final int SAMPLE_RATE = 44100;

    private AssetManager assets;
    private Executor executor;
    private SourceDataLine line;

    private int currentPriority;
    private boolean enabled;

    public SoundManager(AssetManager assetManager) {
        this(assetManager, Executors.newSingleThreadExecutor(), createLine());
    }

    SoundManager(AssetManager assetManager, Executor executor, SourceDataLine line) {
        this.assets = assetManager;
        this.executor = executor;
        this.line = line;

        currentPriority = Integer.MIN_VALUE;
        enabled = true;
    }

    private static SourceDataLine createLine() {
        try {
            return AudioSystem.getSourceDataLine(new AudioFormat(SAMPLE_RATE, 8, 1, true, true));
        } catch (LineUnavailableException e) {
            throw new DukeNukemException("Could not obtain audio line", e);
        }
    }

    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        try {
            line.open();
        } catch (LineUnavailableException e) {
            throw new DukeNukemException("Could not open audio line", e);
        }

        line.start();
    }

    public void play(int index) {
        Sound sound = assets.getSounds().get(index);

        play(sound);
    }

    private void play(Sound sound) {
        if (enabled && (sound.priority() >= currentPriority)) {
            executor.execute(() -> {
                setCurrentPriority(sound.priority());

                byte[] data = sound.data();

                line.write(data, 0, data.length);
                line.drain();

                setCurrentPriority(Integer.MIN_VALUE);
            });
        }
    }

    public void toggle() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    void setCurrentPriority(int priority) {
        this.currentPriority = priority;
    }

    public void shutdown() {
        line.flush();
        line.close();
    }

    public static final int SFX_BOLT_INDEX = 6;
    public static final int SFX_JUMP_INDEX = 13;
}

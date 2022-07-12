package duke.sounds;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Sfx {
    public static final int SAMPLE_RATE = 44100;

    private SourceDataLine line;

    private List<Sound> sounds;

    private int currentPriority;
    private Executor executor;

    private boolean enabled;

    public Sfx(List<Sound> sounds) {
        this.sounds = sounds;

        currentPriority = Integer.MIN_VALUE;
        executor = Executors.newSingleThreadExecutor();

        enabled = true;
    }

    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        try {
            line = AudioSystem.getSourceDataLine(new AudioFormat(SAMPLE_RATE, 8, 1, true, true));
            line.open();
            line.start();
        } catch (LineUnavailableException e) {
            throw new RuntimeException("Could not initialize sfx", e);
        }
    }

    public void play(int index) {
        Sound sound = sounds.get(index);

        if (enabled && (sound.getPriority() >= currentPriority)) {
            executor.execute(() -> {
                byte[] data = sound.getData();

                line.write(data, 0, data.length);
                line.drain();

                currentPriority = Integer.MIN_VALUE;
            });
        }
    }

    public void shutdown() {
        line.flush();
        line.close();
    }

    public void toggle() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}

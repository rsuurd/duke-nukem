package duke.sfx;

import duke.DukeNukemException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PcSpeaker {
    private static final int RATE = 144;
    private static final int SAMPLE_RATE = 11025;
    private static final int PIT_FREQUENCY = 1193181;
    private static final int SAMPLES_PER_TICK = SAMPLE_RATE / RATE;
    private static final long DELAY = Duration.ofSeconds(1).toNanos() / RATE;

    private ScheduledExecutorService executor;
    private SourceDataLine line;

    private Sound current;
    private int currentIndex;

    private double phase = 0.0;

    public PcSpeaker() {
        this(createLine(), Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory()));
    }

    PcSpeaker(SourceDataLine line, ScheduledExecutorService executor) {
        this.line = line;
        this.executor = executor;
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

        executor.scheduleAtFixedRate(this::sendNextAudioSample, 0L, DELAY, TimeUnit.NANOSECONDS);
    }

    public void play(Sound sound) {
        if (current == null || sound.priority() >= current.priority()) {
            this.current = sound;
            this.currentIndex = 0;
        }
    }

    private void reset() {
        current = null;
        currentIndex = 0;
    }

    void sendNextAudioSample() {
        if (current == null) return;

        if (currentIndex < current.frequencies().length) {
            int frequency = current.frequencies()[currentIndex++];
            byte[] decoded = decode(frequency);
            line.write(decoded, 0, decoded.length);
        } else {
            reset();
        }
    }

    private byte[] decode(int value) {
        if (value == 0) {
            phase = 0.0;
            return SILENCE;
        }

        int frequency = PIT_FREQUENCY / value;
        byte[] data = new byte[SAMPLES_PER_TICK];
        double phaseIncrement = 2.0 * Math.PI * frequency / SAMPLE_RATE;

        for (int i = 0; i < SAMPLES_PER_TICK; i++) {
            data[i] = (byte) (Math.sin(phase) >= 0 ? 127 : -127);
            phase += phaseIncrement;
        }

        return data;
    }

    public void shutdown() {
        executor.shutdown();
        line.flush();
        line.close();
    }

    Sound getCurrent() {
        return current;
    }

    private static final byte[] SILENCE = new byte[SAMPLES_PER_TICK];
}

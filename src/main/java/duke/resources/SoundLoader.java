package duke.resources;

import duke.sfx.Sound;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static duke.sfx.SoundManager.SAMPLE_RATE;
import static java.lang.Math.PI;

public class SoundLoader {
    private Path path;

    public SoundLoader(Path path) {
        this.path = path;
    }

    public List<Sound> readSounds() {
        List<Sound> sounds = new ArrayList<>();

        sounds.addAll(readSounds("DUKE1.DN1"));
        sounds.addAll(readSounds("DUKE1-B.DN1"));

        return sounds;
    }

    private List<Sound> readSounds(String file) {
        List<Sound> sounds = new ArrayList<>();

        try (RandomAccessFile in = new RandomAccessFile(path.resolve(file).toFile(), "r")) {
            in.skipBytes(16);

            for (int i = 0; i < 23; i++) {
                Sound sound = readSound(in);

                if (sound != null) {
                    sounds.add(sound);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read sounds from " + file, e);

        }

        return sounds;
    }

    private Sound readSound(RandomAccessFile in) throws IOException {
        int offset = read(in);
        int priority = in.readUnsignedByte();
        in.skipBytes(1);

        if (shouldSkip(in)) return null;

        long current = in.getFilePointer();
        in.seek(offset);

        byte[] data = readData(in);

        in.seek(current);

        return new Sound(priority, data);
    }

    private boolean shouldSkip(RandomAccessFile in) throws IOException {
        return "__UnNamed__".equals(readName(in));
    }

    private String readName(RandomAccessFile in) throws IOException {
        byte[] bytes = new byte[12];
        in.read(bytes);

        return new String(bytes).trim();
    }

    private byte[] readData(RandomAccessFile in) throws IOException {
        int value;

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while ((value = read(in)) != 0xFFFF) {
            int frequency = (value == 0) ? 0 : 1193181 / value;

            byte[] data = new byte[(int) ((1000f / 144) * SAMPLE_RATE) / 1000];

            if (frequency == 0) {
                Arrays.fill(data, (byte) 0);
            } else {
                double period = (double) SAMPLE_RATE / frequency;

                for (int j = 0; j < data.length; j++) {
                    double angle = 2.0 * PI * j / period;

                    data[j] = (byte) (Math.signum(Math.sin(angle)) * 127f);
                }
            }

            out.write(data);
        }

        return out.toByteArray();
    }

    private int read(RandomAccessFile in) throws IOException {
        return Short.toUnsignedInt(Short.reverseBytes(in.readShort()));
    }
}

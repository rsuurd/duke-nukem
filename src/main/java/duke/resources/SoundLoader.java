package duke.resources;

import duke.sfx.Sound;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

        String name = readName(in);
        if (shouldSkip(name)) return null;

        long current = in.getFilePointer();
        in.seek(offset);

        int[] frequencies = readFrequencies(in);

        in.seek(current);

        return new Sound(priority, frequencies);
    }

    private String readName(RandomAccessFile in) throws IOException {
        byte[] bytes = new byte[12];
        in.read(bytes);

        return new String(bytes).trim();
    }

    private boolean shouldSkip(String name) {
        return "__UnNamed__".equalsIgnoreCase(name);
    }

    private int[] readFrequencies(RandomAccessFile in) throws IOException {
        List<Integer> frequencies = new ArrayList<>();

        int value;

        while ((value = read(in)) != 0xFFFF) {
            frequencies.add(value);
        }

        return frequencies.stream().mapToInt(i -> i).toArray();
    }

    private int read(RandomAccessFile in) throws IOException {
        return Short.toUnsignedInt(Short.reverseBytes(in.readShort()));
    }
}

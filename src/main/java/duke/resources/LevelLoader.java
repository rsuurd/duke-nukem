package duke.resources;

import duke.DukeNukemException;
import duke.level.LevelData;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class LevelLoader {
    private Path path;

    public LevelLoader(Path path) {
        this.path = path;
    }

    public LevelData readLevel(int number) {
        String name = String.format("WORLDAL%x.DN1", number);

        try (RandomAccessFile in = new RandomAccessFile(path.resolve(name).toFile(), "r")) {
            int size = (int) (in.length() / 2);
            int[] data = new int[size];

            for (int i = 0; i < data.length; i++) {
                data[i] = Short.reverseBytes(in.readShort());
            }

            return new LevelData(data);
        } catch (IOException e) {
            throw new DukeNukemException(String.format("Could not read %s", name), e);
        }
    }
}

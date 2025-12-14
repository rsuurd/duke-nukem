package duke.resources;

import duke.DukeNukemException;
import duke.gfx.Sprite;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SpriteLoader {
    private Path path;

    public SpriteLoader(Path path) {
        this.path = path;
    }

    public Sprite readImage(String name) {
        try (RandomAccessFile in = new RandomAccessFile(path.resolve(name).toFile(), "r")) {
            int width = 320;
            int height = 200;
            byte[] pixels = new byte[width * height];

            int size = (int) in.length() / 4;
            byte[] intensities = new byte[size];
            byte[] reds = new byte[size];
            byte[] greens = new byte[size];
            byte[] blues = new byte[size];

            in.read(intensities);
            in.read(reds);
            in.read(greens);
            in.read(blues);

            int x = 0;
            int y = 0;

            for (int i = 0; i < size; i++) {
                BigInteger intensity = BigInteger.valueOf(intensities[i]);
                BigInteger red = BigInteger.valueOf(reds[i]);
                BigInteger green = BigInteger.valueOf(greens[i]);
                BigInteger blue = BigInteger.valueOf(blues[i]);

                for (int bit = 7; bit >= 0; bit--) {
                    byte index = toIndex(intensity.testBit(bit), red.testBit(bit), green.testBit(bit), blue.testBit(bit));

                    pixels[y * width + x] = index;

                    x++;

                    if (x >= width) {
                        x = 0;
                        y++;
                    }
                }
            }

            return new Sprite(width, height, pixels);
        } catch (IOException e) {
            throw new DukeNukemException(String.format("Could not read image: %s", name), e);
        }
    }

    public Sprite readBackdrop(int number) {
        try (RandomAccessFile in = new RandomAccessFile(path.resolve(String.format("DROP%d.DN1", number)).toFile(), "r")) {
            in.seek(3);

            int width = 208;
            int height = 160;
            int tileSize = 16;
            byte[] pixels = new byte[width * height];

            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 13; col++) {
                    Sprite tile = readTile(2, 16, in, true);

                    for (int y = 0; y < tileSize; y++) {
                        for (int x = 0; x < tileSize; x++) {
                            byte pixel = tile.getPixel(x, y);

                            pixels[((row * tileSize) + y) * width + (col * tileSize) + x] = pixel;
                        }
                    }
                }
            }

            return new Sprite(width, height, pixels);
        } catch (IOException e) {
            throw new DukeNukemException(String.format("Could not read DROP%d.DN1", number), e);
        }
    }

    public List<Sprite> readTiles(String name, boolean opaque) {
        try (RandomAccessFile in = new RandomAccessFile(path.resolve(name).toFile(), "r")) {
            int count = in.readByte();
            int widthInBytes = in.readByte();
            int heightInPixels = in.readByte();

            List<Sprite> tiles = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                tiles.add(readTile(widthInBytes, heightInPixels, in, opaque));
            }

            return tiles;
        } catch (IOException e) {
            throw new DukeNukemException(String.format("Could not read %s", name), e);
        }
    }

    private Sprite readTile(int widthInBytes, int height, RandomAccessFile in, boolean opaque) throws IOException {
        byte[] data = new byte[widthInBytes * height * 5];

        if (in.read(data) != -1) {
            int width = widthInBytes * 8;
            byte[] pixels = new byte[width * height];

            int y = 0;
            int x = 0;

            for (int i = 0; i < data.length; ) {
                BigInteger opacity = BigInteger.valueOf(data[i++]);
                BigInteger intensity = BigInteger.valueOf(data[i++]);
                BigInteger red = BigInteger.valueOf(data[i++]);
                BigInteger green = BigInteger.valueOf(data[i++]);
                BigInteger blue = BigInteger.valueOf(data[i++]);

                for (int bit = 7; bit >= 0; bit--) {
                    if (opaque || opacity.testBit(bit)) {
                        byte index = toIndex(intensity.testBit(bit), red.testBit(bit), green.testBit(bit), blue.testBit(bit));

                        pixels[y * width + x] = index;
                    }

                    x++;

                    if (x >= width) {
                        x = 0;
                        y++;
                    }
                }
            }

            return new Sprite(width, height, pixels, opaque);
        } else {
            throw new DukeNukemException("Could not read tile");
        }
    }

    private byte toIndex(boolean intensity, boolean red, boolean green, boolean blue) {
        byte index = 0;

        if (intensity) {
            index |= 0b0001;
        }

        if (red) {
            index |= 0b0010;
        }

        if (green) {
            index |= 0b0100;
        }

        if (blue) {
            index |= 0b1000;
        }

        return index;
    }
}

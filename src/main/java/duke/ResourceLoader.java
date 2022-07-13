package duke;

import duke.sounds.Sound;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static duke.sounds.Sfx.SAMPLE_RATE;
import static java.lang.Math.PI;

public class ResourceLoader {
    private Path path;

    public ResourceLoader(Path path) {
        this.path = path;
    }

    public void init() {
        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }

            if (path.toFile().listFiles().length == 0) {
                try (ZipInputStream zip = new ZipInputStream(new URL("https://image.dosgamesarchive.com/games/1duke.zip").openStream())) {
                    ZipEntry entry;

                    while ((entry = zip.getNextEntry()) != null) {
                        if (entry.getName().equalsIgnoreCase("DUKE.1")) {
                            zip.skip(0x3bf5);

                            ZipInputStream nested = new ZipInputStream(zip);

                            while ((entry = nested.getNextEntry()) != null) {
                                Path file = path.resolve(entry.getName());

                                if (entry.isDirectory()) {
                                    Files.createDirectory(file);
                                } else {
                                    Files.copy(nested, file);
                                }

                                nested.closeEntry();
                            }
                        }

                        zip.closeEntry();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize ResourceLoader", e);
        }
    }

    public BufferedImage readFullscreenImage(String name) {
        try (RandomAccessFile in = new RandomAccessFile(path.resolve(name).toFile(), "r")) {
            BufferedImage image = new BufferedImage(320, 200, BufferedImage.TYPE_INT_ARGB);

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
                    int index = toIndex(intensity.testBit(bit), red.testBit(bit), green.testBit(bit), blue.testBit(bit));

                    image.setRGB(x, y, EGA_PALETTE[index]);

                    x++;

                    if (x >= image.getWidth()) {
                        x = 0;
                        y++;
                    }
                }
            }

            return image;
        } catch (IOException e) {
            throw new RuntimeException("Could not read: " + name, e);
        }
    }

    public List<BufferedImage> readTiles() {
        return Stream.of("BACK0.DN1", "BACK1.DN1", "BACK2.DN1", "BACK3.DN1",
                "SOLID0.DN1", "SOLID1.DN1", "SOLID2.DN1", "SOLID3.DN1").flatMap(name -> readTiles(name, true).stream().limit(48)).collect(Collectors.toList());
    }

    public List<BufferedImage> readMan() {
        return Stream.of("MAN0.DN1", "MAN1.DN1", "MAN2.DN1", "MAN3.DN1").flatMap(name -> readTiles(name, false).stream()).collect(Collectors.toList());
    }

    public List<BufferedImage> readFont() {
        return Stream.of("FONT1.DN1", "FONT2.DN1").flatMap(name -> readTiles(name, false).stream()).collect(Collectors.toList());
    }

    public List<BufferedImage> readAnim() {
        return Stream.of("ANIM0.DN1", "ANIM1.DN1", "ANIM2.DN1", "ANIM3.DN1", "ANIM4.DN1", "ANIM5.DN1").flatMap(name -> readTiles(name, false).stream()).collect(Collectors.toList());
    }

    public List<BufferedImage> readObject() {
        return Stream.of("OBJECT0.DN1", "OBJECT1.DN1", "OBJECT2.DN1").flatMap(name -> readTiles(name, false).stream()).collect(Collectors.toList());
    }

    public List<BufferedImage> readBorder() {
        return readTiles("BORDER.DN1", true);
    }

    public List<BufferedImage> readNumbers() {
        return readTiles("NUMBERS.DN1", false);
    }

    public List<BufferedImage> readTiles(String name, boolean opaque) {
        try (RandomAccessFile in = new RandomAccessFile(path.resolve(name).toFile(), "r")) {
            int count = in.readByte();
            int widthInBytes = in.readByte();
            int heightInPixels = in.readByte();

            List<BufferedImage> tiles = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                tiles.add(readTile(widthInBytes, heightInPixels, in, opaque));
            }

            return tiles;
        } catch (IOException e) {
            throw new RuntimeException("Could not read: " + name, e);
        }
    }

    public BufferedImage readBackdrop(String name) {
        try (RandomAccessFile in = new RandomAccessFile(path.resolve(name).toFile(), "r")) {
            in.seek(3);

            BufferedImage backdrop = new BufferedImage(208, 160, BufferedImage.TYPE_INT_ARGB);

            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 13; col++) {
                    backdrop.getGraphics().drawImage(readTile(2, 16, in, true), col * 16, row * 16, null);
                }
            }

            return backdrop;
        } catch (IOException e) {
            throw new RuntimeException("Could not read: " + name, e);
        }
    }

    private BufferedImage readTile(int widthInBytes, int heightInPixels, RandomAccessFile in, boolean opaque) throws IOException {
        byte[] data = new byte[widthInBytes * heightInPixels * 5];

        if (in.read(data) != -1) {
            BufferedImage tile = new BufferedImage(widthInBytes * 8, heightInPixels, BufferedImage.TYPE_INT_ARGB);

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
                        int index = toIndex(intensity.testBit(bit), red.testBit(bit), green.testBit(bit), blue.testBit(bit));

                        tile.setRGB(x, y, EGA_PALETTE[index]);
                    }

                    x++;

                    if (x >= tile.getWidth()) {
                        x = 0;
                        y++;
                    }
                }
            }

            return tile;
        } else {
            throw new IOException("Unexpected end of file");
        }
    }

    private int toIndex(boolean intensity, boolean red, boolean green, boolean blue) {
        int index = 0;

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

    private static final int[] EGA_PALETTE = new int[]{
            0xFF000000,
            0xFF0000AA,
            0xFF00AA00,
            0xFF00AAAA,
            0xFFAA0000,
            0xFFAA00AA,
            0xFFAA5500,
            0xFFAAAAAA,
            0xFF555555,
            0xFF5555FF,
            0xFF55FF55,
            0xFF55FFFF,
            0xFFFF5555,
            0xFFFF55FF,
            0xFFFFFF55,
            0xFFFFFFFF
    };

    public Level readLevel(int number) {
        int suffix;

        if (number == 0) {
            suffix = 1;
        } else if ((number % 2) == 1) {
            suffix = 2;
        } else {
            suffix = (number / 2) + 2;
        }

        try (RandomAccessFile in = new RandomAccessFile(path.resolve(String.format("WORLDAL%x.DN1", suffix)).toFile(), "r")) {
            BufferedImage backdrop = readBackdrop("DROP0.DN1");

            int[] tiles = new int[Level.WIDTH * Level.HEIGHT];

            for (int i = 0; i < tiles.length; i++) {
                tiles[i] = Short.reverseBytes(in.readShort());
            }

            return new Level(number, tiles, backdrop);
        } catch (IOException e) {
            throw new RuntimeException("Could not read level " + number, e);
        }
    }

    public List<Sound> readSounds() {
        List<Sound> sounds = new ArrayList<>();

        Stream.of("DUKE1.DN1", "DUKE1-B.DN1").forEach(file -> {
            try (RandomAccessFile in = new RandomAccessFile(path.resolve(file).toFile(), "r")) {
                in.skipBytes(16);

                for (int i = 0; i < 23; i ++) {
                    int offset = Short.toUnsignedInt(Short.reverseBytes(in.readShort()));
                    int priority = in.readUnsignedByte();
                    in.skipBytes(1);

                    byte[] bytes = new byte[12];
                    in.read(bytes);

                    String name = new String(bytes).trim();

                    if ("__UnNamed__".equals(name)) {
                        continue;
                    }

                    long current = in.getFilePointer();
                    in.seek(offset);

                    int value;

                    ByteArrayOutputStream out = new ByteArrayOutputStream();

                    while ((value = Short.toUnsignedInt(Short.reverseBytes(in.readShort()))) != 0xFFFF) {
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

                    sounds.add(new Sound(priority, out.toByteArray()));

                    in.seek(current);
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not read sounds", e);
            }
        });

        return sounds;
    }

    public void save(char slot, GameState state) {
        try (RandomAccessFile out = new RandomAccessFile(path.resolve(String.format("SAVED%s.DN1", slot)).toFile(), "rw")) {

            out.writeShort(Short.reverseBytes((short) 0)); // these are ignored anyway
            out.writeShort(Short.reverseBytes((short) 0));

            out.writeShort(Short.reverseBytes((short) (state.getDuke().getX() / 8)));
            out.writeShort(Short.reverseBytes((short) (state.getDuke().getY() / 8)));
            out.writeShort(Short.reverseBytes((short) state.getLevel().getNumber()));
            out.writeShort(Short.reverseBytes((short) state.getDuke().getFirePower()));
            out.writeShort(Short.reverseBytes((short) 0)); // boots
            out.writeShort(Short.reverseBytes((short) 0)); // hook
            out.writeShort(Short.reverseBytes((short) (state.getDuke().getHealth() + 1)));
            out.writeShort(Short.reverseBytes((short) 0)); // robohand

            for (int i = 0; i < 12; i ++) {
                out.writeShort(Short.reverseBytes((short) 0)); // tips
            }

            for (char c: Integer.toString(state.getScore()).toCharArray()) {
                out.writeByte((byte) c);
            }

            out.write('\n');
        } catch (IOException e) {
            throw new RuntimeException("Could not save game", e);
        }
    }

    public BufferedImage toImage(Level level) {
        List<BufferedImage> tileSet = readTiles();

        BufferedImage map = new BufferedImage(Level.WIDTH * 16, Level.HEIGHT * 16, BufferedImage.TYPE_INT_ARGB);

        for (int row = 0; row < Level.HEIGHT; row++) {
            for (int col = 0; col < Level.WIDTH; col++) {
                int tileId = level.getTile(row, col);

                if (tileId < 0x3000) {
                    map.getGraphics().drawImage(tileSet.get(tileId / 32), col * 16, row * 16, null);
                }
            }
        }

        return map;
    }

    private BufferedImage toSheet(List<BufferedImage> tiles) {
        BufferedImage first = tiles.get(0);

        int width = first.getWidth();
        int height = first.getHeight();

        BufferedImage image = new BufferedImage(tiles.size() * width, height, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < tiles.size(); i++) {
            image.getGraphics().drawImage(tiles.get(i), i * width, 0, null);
        }
        return image;
    }

    public static void main(String[] args) throws IOException {
        ResourceLoader l = new ResourceLoader(Path.of(".dn1"));

        l.readSounds();
    }
}


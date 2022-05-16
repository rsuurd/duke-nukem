package duke;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
                    }

                    zip.closeEntry();
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
}

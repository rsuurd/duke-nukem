package duke;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class DukeNukem {
    public static void main(String... parameters) throws IOException {
        ResourceLoader loader = new ResourceLoader(Paths.get(".dn1"));
        loader.init();

        for (int i = 1; i <= 0xc; i ++) {
            ImageIO.write(loader.toImage(loader.readLevel(i)), "PNG", new File(String.format("WORLDAL%x.PNG", i)));
        }
    }
}

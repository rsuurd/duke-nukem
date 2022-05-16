package duke;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class DukeNukem {
    public static void main(String... parameters) throws IOException {
        ResourceLoader loader = new ResourceLoader(Paths.get(".dn1"));
        loader.init();

        List<BufferedImage> tiles = loader.readTiles("OBJECT0.DN1");

        for (int i = 0; i < tiles.size(); i++ ){
            ImageIO.write(tiles.get(i), "PNG", new File("OBJECT-" + i + ".png"));
        }
    }
}

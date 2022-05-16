package duke;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class DukeNukem {
    public static void main(String... parameters) throws IOException {
        ResourceLoader loader = new ResourceLoader(Paths.get(".dn1"));
        loader.init();


        ImageIO.write(loader.readFullscreenImage("DUKE.DN1"), "PNG", new File("DUKE.PNG"));
    }
}

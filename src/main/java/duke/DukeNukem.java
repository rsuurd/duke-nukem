package duke;

import duke.resources.ResourceLoader;
import duke.ui.DukeNukemFrame;

import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DukeNukem {
    private ResourceLoader resourceLoader;

    public DukeNukem(ResourceLoader resourceLoader, Renderer renderer) {
        this.resourceLoader = resourceLoader;
    }

    public static void main(String... parameters) {
        Path basePath = Paths.get(".dn1"); // TODO read from config or parameters

        SwingUtilities.invokeLater(() -> {
            DukeNukemFrame frame = new DukeNukemFrame();

            ResourceLoader resourceLoader = new ResourceLoader(basePath);

            DukeNukem dukeNukem = new DukeNukem(resourceLoader, frame.getRenderer());
        });
    }
}

package duke.ui;

import duke.Renderer;

import javax.swing.*;

public class DukeNukemFrame extends JFrame {
    private CanvasRenderer renderer;

    public DukeNukemFrame() {
        super("Duke Nukem");

        renderer = new CanvasRenderer();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(renderer);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Renderer getRenderer() {
        return renderer;
    }
}

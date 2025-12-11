package duke.ui;

import duke.Renderer;
import duke.gfx.EgaPalette;

import javax.swing.*;

public class DukeNukemFrame extends JFrame {
    private CanvasRenderer renderer;

    public DukeNukemFrame(KeyHandler keyHandler, EgaPalette palette) {
        super("Duke Nukem");

        renderer = new CanvasRenderer(palette);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(renderer);
        pack();

        renderer.addKeyListener(keyHandler);
        renderer.requestFocus();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Renderer getRenderer() {
        return renderer;
    }
}

package duke.ui;

import javax.swing.*;

public class DukeNukemFrame extends JFrame {
    public DukeNukemFrame(CanvasRenderer renderer, KeyHandler keyHandler) {
        super("Duke Nukem");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(renderer);
        pack();

        renderer.addKeyListener(keyHandler);
        renderer.requestFocus();

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

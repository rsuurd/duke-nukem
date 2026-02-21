package duke.state.storyboard;

import java.util.List;

public class Storyboard {
    private List<Panel> panels;

    private int index;

    public Storyboard(List<Panel> panels) {
        this.panels = panels;

        index = 0;
    }

    public Panel current() {
        return panels.get(index);
    }

    public boolean hasNext() {
        return index < panels.size() - 1;
    }

    public void advance() {
        if (!hasNext()) {
            throw new IllegalStateException("No more panels in the storyboard");
        }

        index++;
    }
}

package duke.gameplay;

import duke.Renderer;
import duke.gfx.SpriteRenderer;
import duke.gfx.Viewport;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BoltManager {
    private Viewport viewport;
    private SpriteRenderer spriteRenderer;

    private List<Bolt> bolts;

    public BoltManager(Viewport viewport, SpriteRenderer spriteRenderer) {
        this.viewport = viewport;
        this.spriteRenderer = spriteRenderer;

        bolts = new LinkedList<>();
    }

    public void update(GameplayContext context) {
        Iterator<Bolt> iterator = bolts.iterator();

        while (iterator.hasNext()) {
            Bolt bolt = iterator.next();

            bolt.update(context);

            if (bolt.isDestroyed()) {
                iterator.remove();
            }
        }
    }

    public void spawn(Bolt bolt) {
        bolts.add(bolt);
    }

    public void render(Renderer renderer) {
        for (Bolt bolt : bolts) {
            if (!viewport.isVisible(bolt)) continue;

            int x = viewport.toScreenX(bolt.getX());
            int y = viewport.toScreenY(bolt.getY());

            spriteRenderer.render(renderer, bolt, x, y);
        }
    }

    List<Bolt> getBolts() {
        return bolts;
    }
}

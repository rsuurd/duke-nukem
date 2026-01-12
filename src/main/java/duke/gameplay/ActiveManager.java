package duke.gameplay;

import duke.Renderer;
import duke.gfx.Renderable;
import duke.gfx.SpriteRenderable;
import duke.gfx.SpriteRenderer;
import duke.gfx.Viewport;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ActiveManager {
    private Viewport viewport;
    private Collision collision;
    private SpriteRenderer spriteRenderer;

    private List<Active> actives;
    private List<Active> spawns;

    public ActiveManager(Viewport viewport, Collision collision, SpriteRenderer spriteRenderer) {
        this.viewport = viewport;
        this.collision = collision;
        this.spriteRenderer = spriteRenderer;

        actives = new LinkedList<>();
        spawns = new LinkedList<>();
    }

    public void update(GameplayContext context) {
        updateActives(context);
        addPendingSpawns();
    }

    private void updateActives(GameplayContext context) {
        Iterator<Active> iterator = actives.iterator();

        while (iterator.hasNext()) {
            Active active = iterator.next();

            wakeUpIfNeeded(active);
            update(active, context);

            if (active.isDestroyed()) {
                iterator.remove();
            }
        }
    }

    private void wakeUpIfNeeded(Active active) {
        if (!active.isActivated() && viewport.isVisible(active)) {
            active.activate();
        }
    }

    private void update(Active active, GameplayContext context) {
        if (!active.isActivated() || active.isDestroyed()) return;

        if (active instanceof Updatable updatable) {
            updatable.update(context);
        }

        if (active instanceof Physics body) {
            collision.resolve(body, context);
        }
    }

    public void spawn(Active active) {
        spawns.add(active);
    }

    public void spawn(List<? extends Active> actives) {
        spawns.addAll(actives);
    }

    private void addPendingSpawns() {
        actives.addAll(spawns);
        spawns.clear();
    }

    public void render(Renderer renderer, Layer layer) {
        for (Active active : actives) {
            if (!(active instanceof Renderable renderable)) continue;
            if (renderable.getLayer() != layer) continue;
            if (!viewport.isVisible(active)) continue;

            int x = viewport.toScreenX(active.getX());
            int y = viewport.toScreenY(active.getY());

            renderable.render(renderer, spriteRenderer, x, y);
        }
    }

    public List<Active> getActives() {
        return actives;
    }

    List<Active> getSpawns() {
        return spawns;
    }
}

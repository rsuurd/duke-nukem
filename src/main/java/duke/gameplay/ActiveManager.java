package duke.gameplay;

import duke.Renderer;
import duke.gameplay.effects.Effect;
import duke.gfx.SpriteRenderable;
import duke.gfx.SpriteRenderer;
import duke.gfx.Viewport;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ActiveManager {
    private Collision collision;

    private List<Active> actives;
    private List<Active> spawns;

    public ActiveManager(Collision collision) {
        this.collision = collision;

        actives = new LinkedList<>();
        spawns = new LinkedList<>();
    }

    public void update(GameplayContext context) {
        Iterator<Active> iterator = actives.iterator();

        while (iterator.hasNext()) {
            Active active = iterator.next();

            if (active instanceof Updatable updatable) {
                updatable.update(context);
            }

            if (active instanceof Physics body) {
                collision.resolve(body, context.getLevel());
            }

            if (!active.isActive()) {
                iterator.remove();
            }
        }

        addPendingSpawns();
    }

    public void spawn(Bolt bolt) {
        // FIXME temporary workaround for bolt spawn/update frame delay
        actives.add(bolt);
    }

    public void spawn(Active active) {
        spawns.add(active);
    }

    public void spawn(List<? extends Active> effects) {
        spawns.addAll(effects);
    }

    private void addPendingSpawns() {
        actives.addAll(spawns);
        spawns.clear();
    }

    public void render(Renderer renderer, SpriteRenderer spriteRenderer, Viewport viewport, Layer layer) {
        for (Active active : actives) {
            if (!(active instanceof SpriteRenderable renderable)) continue;
            if (renderable.getLayer() != layer) continue;
            if (!viewport.isVisible(active)) continue;

            int x = viewport.toScreenX(active.getX());
            int y = viewport.toScreenY(active.getY());

            spriteRenderer.render(renderer, renderable, x, y);
        }
    }

    List<Active> getActives() {
        return actives;
    }

    List<Active> getSpawns() {
        return spawns;
    }
}

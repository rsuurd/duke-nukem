package duke.gameplay;

import duke.Renderer;
import duke.gameplay.effects.Sparks;
import duke.gfx.SpriteRenderer;
import duke.gfx.Viewport;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ActiveManager {
    private List<Active> actives = new LinkedList<>();
    private List<Active> spawns = new LinkedList<>();

    public void update(GameplayContext context, Viewport viewport) {
        Iterator<Active> iterator = actives.iterator();

        while (iterator.hasNext()) {
            Active active = iterator.next();

            if (viewport.isVisible(active)) {
                if (active instanceof Updatable updatable) {
                    updatable.update(context);
                }
            }

            if (active instanceof Bolt bolt && !bolt.isActive()) {
                iterator.remove();
            }

            if (active instanceof Sparks sparks && sparks.getAnimation().isFinished()) {
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

    private void addPendingSpawns() {
        actives.addAll(spawns);
        spawns.clear();
    }

    public void render(Renderer renderer, SpriteRenderer spriteRenderer, Viewport viewport) {
        actives.forEach(active -> {
            // TODO should probably let spriterenderer do some visiblity checks
            if (viewport.isVisible(active) && active instanceof SpriteRenderable renderable) {
                int x = viewport.toScreenX(active.getX());
                int y = viewport.toScreenY(active.getY());

                spriteRenderer.render(renderer, renderable, x, y);
            }
        });
    }

    List<Active> getActives() {
        return actives;
    }

    List<Active> getSpawns() {
        return spawns;
    }
}

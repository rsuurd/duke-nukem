package duke.gameplay;

import duke.Renderer;
import duke.gameplay.active.Wakeable;
import duke.gameplay.active.enemies.EnemyFire;
import duke.gameplay.player.Player;
import duke.gfx.Renderable;
import duke.gfx.SpriteRenderer;
import duke.gfx.Viewport;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static duke.sfx.Sfx.PLAYER_HIT;

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
        updateInteraction(context);
        updateActives(context);
        addPendingSpawns();
    }

    private void updateInteraction(GameplayContext context) {
        Player player = context.getPlayer();

        if (player.isUsing()) {
            for (Active active : actives) {
                if (active instanceof Interactable interactable && interactable.canInteract(player)) {
                    interactable.interactRequested(context);

                    return;
                }
            }
        }
    }

    private void updateActives(GameplayContext context) {
        Iterator<Active> iterator = actives.iterator();

        while (iterator.hasNext()) {
            Active active = iterator.next();

            boolean visible = viewport.isVisible(active);

            wakeUpIfNeeded(active, visible);
            update(active, context, visible);
            checkDamage(active, context);

            if (active.isDestroyed()) {
                iterator.remove();
            }
        }
    }

    private void wakeUpIfNeeded(Active active, boolean visible) {
        if (visible && active instanceof Wakeable wakeable && !wakeable.isAwake()) {
            wakeable.wakeUp();
        }
    }

    private void update(Active active, GameplayContext context, boolean visible) {
        if (!shouldUpdate(active, visible)) return;

        if (active instanceof Updatable updatable) {
            updatable.update(context);
        }

        if (active instanceof Physics body) {
            collision.resolve(body, context);
        }
    }

    private boolean shouldUpdate(Active active, boolean visible) {
        if (active.isDestroyed()) return false;
        if (active instanceof Wakeable wakeable) {
            return wakeable.isAwake();
        }
        return visible;
    }

    private void checkDamage(Active active, GameplayContext context) {
        if (active.isDestroyed()) return;

        Player player = context.getPlayer();

        if (player.getHealth().isInvulnerable()) return;
        if (active instanceof Damaging damaging && damaging.getDamage() > 0 && player.intersects(active)) {
            player.getHealth().takeDamage(damaging);
            context.getSoundManager().play(PLAYER_HIT);

            // TODO this despawns on touch, should probably hook into collision system later
            if (active instanceof EnemyFire enemyFire) enemyFire.destroy();
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

package duke.gameplay.active;

import duke.Renderer;
import duke.dialog.Dialog;
import duke.gameplay.*;
import duke.gameplay.player.Player;
import duke.gfx.*;
import duke.sfx.Sfx;

import static duke.gameplay.active.Monitor.State.READY;
import static duke.gameplay.active.Monitor.State.TRACKING;
import static duke.level.Level.TILE_SIZE;

public class Monitor extends Active implements Renderable, Interactable, Updatable, Visibility {
    private SpriteDescriptor screen;
    private Animation animation;

    private State state;

    public Monitor(int x, int y) {
        this(x, y, READY);
    }

    Monitor(int x, int y, State state) {
        super(x, y - TILE_SIZE, 32, 32);

        animation = new Animation(STATIC);
        screen = animation.getSpriteDescriptor();

        this.state = state;
    }

    @Override
    public boolean canInteract(Player player) {
        return player.intersects(this);
    }

    @Override
    public void interactRequested(GameplayContext context) {
        broadcast(context);
    }

    @Override
    public void update(GameplayContext context) {
        broadcastWhenReady(context);
        determineScreen(context.getPlayer());
    }

    private void broadcastWhenReady(GameplayContext context) {
        if (state != READY) return;

        if (isPlayerClose(context.getPlayer())) {
            broadcast(context);
        }
    }

    private boolean isPlayerClose(Player player) {
        int distance = Math.abs((getX() + (getWidth() / 2)) - (player.getX() + (player.getWidth() / 2)));

        return distance < DISTANCE_TO_BROADCAST;
    }

    private void broadcast(GameplayContext context) {
        state = TRACKING;
        context.getDialogManager().open(Dialog.notes(context.getLevel().getDescriptor().message()));
        context.getSoundManager().play(Sfx.MONITOR);
    }

    private void determineScreen(Player player) {
        if (state == TRACKING) {
            trackPlayer(player);
        } else {
            displayStatic();
        }
    }

    private void trackPlayer(Player player) {
        if (player.getX() < getX()) {
            screen = FACE_LEFT;
        } else if (player.getX() + player.getWidth() > this.getX() + getWidth()) {
            screen = FACE_RIGHT;
        } else {
            screen = FACE_MID;
        }
    }

    private void displayStatic() {
        animation.tick();
        screen = animation.getSpriteDescriptor();
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        spriteRenderer.render(renderer, screen, screenX, screenY);
        spriteRenderer.render(renderer, BASE, screenX, screenY + TILE_SIZE);
    }

    @Override
    public Layer getLayer() {
        return Layer.BACKGROUND;
    }

    @Override
    public void setVisible(boolean visible) {
        if (!visible && state == TRACKING) {
            state = State.STATIC;
        }
    }

    State getState() {
        return state;
    }

    enum State {
        READY,
        TRACKING,
        STATIC
    }

    private static final int DISTANCE_TO_BROADCAST = 80;
    private static final SpriteDescriptor BASE = new SpriteDescriptor(SpriteDescriptor.ANIM, 266, 0, 0, 1, 2);
    private static final AnimationDescriptor STATIC = new AnimationDescriptor(BASE.withBaseIndex(268), 3, 1);
    private static final SpriteDescriptor FACE_LEFT = BASE.withBaseIndex(274);
    private static final SpriteDescriptor FACE_MID = BASE.withBaseIndex(276);
    private static final SpriteDescriptor FACE_RIGHT = BASE.withBaseIndex(278);
}

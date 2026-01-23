package duke.gameplay.active;

import duke.gameplay.*;
import duke.gameplay.player.Player;
import duke.gfx.Animation;
import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.sfx.Sfx;

public class Exit extends Active implements Updatable, Interactable, SpriteRenderable {
    private State state;
    private Animation animation;

    public Exit(int x, int y) {
        this(x, y, State.CLOSED, new Animation(DESCRIPTOR));
    }

    Exit(int x, int y, State state, Animation animation) {
        super(x, y, 2 * Level.TILE_SIZE, 2 * Level.TILE_SIZE);
        this.state = state;
        this.animation = animation;
    }

    @Override
    public boolean canInteract(Player player) {
        return state == State.CLOSED && player.intersects(this);
    }

    @Override
    public void interactRequested(GameplayContext context) {
        state = State.OPENING;
        context.getPlayer().disableControls();
        Sfx sfx = context.getLevel().getDescriptor().isIntermission() ? Sfx.DOOR_SOUND : Sfx.LEVEL_DONE;
        context.getSoundManager().play(sfx);
    }

    @Override
    public void update(GameplayContext context) {
        switch (state) {
            case OPENING -> open();
            case CLOSING -> close();
            case EXITING -> exit(context);
        }
    }

    private void open() {
        animation.tick();

        if (animation.isFinished()) {
            state = State.CLOSING;
            animation.reverse();
        }
    }

    private void close() {
        animation.tick();

        if (animation.isFinished()) {
            state = State.EXITING;
        }
    }

    private void exit(GameplayContext context) {
        context.getLevel().complete();
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return animation.getSpriteDescriptor();
    }

    @Override
    public Layer getLayer() {
        return state.ordinal() < 2 ? Layer.BACKGROUND : Layer.FOREGROUND;
    }

    State getState() {
        return state;
    }

    enum State {
        CLOSED,
        OPENING,
        CLOSING,
        EXITING
    }

    private static final AnimationDescriptor DESCRIPTOR = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.ANIM, 100, 0, 0, 2, 2), 3, 1, AnimationDescriptor.Type.ONE_SHOT);
}

package duke.gameplay.active;

import duke.Renderer;
import duke.dialog.Hints;
import duke.gameplay.Active;
import duke.gameplay.GameplayContext;
import duke.gameplay.Interactable;
import duke.gameplay.Updatable;
import duke.gameplay.effects.EffectsFactory;
import duke.gameplay.player.Player;
import duke.gfx.*;
import duke.sfx.Sfx;

import java.util.List;

import static duke.level.Level.TILE_SIZE;

public class Transporter extends Active implements Updatable, Renderable, Interactable {
    private int transporting;

    private Animation midPanelAnimation;
    private Animation rightPanelAnimation;

    public Transporter(int x, int y) {
        super(x, y, TILE_SIZE, 2 * TILE_SIZE);

        transporting = 0;
        midPanelAnimation = new Animation(MID_PANEL);
        rightPanelAnimation = new Animation(RIGHT_PANEL);
    }

    @Override
    public boolean canInteract(Player player) {
        return !isTransporting() && player.intersects(this);
    }

    @Override
    public void interactRequested(GameplayContext context) {
        for (Active active : context.getActiveManager().getActives()) {
            if (active != this && active instanceof Transporter transporter) {
                transport(context);
                transporter.transport(context);

                // TODO center camera directly
                context.getPlayer().setX(active.getX());
                context.getPlayer().setY(active.getY());
                context.getSoundManager().play(Sfx.TELEPORT);
                break;
            }
        }
    }

    public void transport(GameplayContext context) {
        transporting = 16;
        context.getActiveManager().spawn(EffectsFactory.createTransporterActiveEffect(getX(), getY() - TILE_SIZE, transporting));
    }

    public boolean isTransporting() {
        return transporting > 0;
    }

    @Override
    public void update(GameplayContext context) {
        if (canInteract(context.getPlayer())) {
            context.getHints().showHint(Hints.Hint.TRANSPORTER, context);
        }

        if (isTransporting()) {
            transporting--;
        }

        midPanelAnimation.tick();
        rightPanelAnimation.tick();
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        spriteRenderer.render(renderer, TRANSPORTER, screenX, screenY);
        spriteRenderer.render(renderer, midPanelAnimation.getSpriteDescriptor(), screenX, screenY);
        spriteRenderer.render(renderer, rightPanelAnimation.getSpriteDescriptor(), screenX + TILE_SIZE, screenY);
    }

    private static final SpriteDescriptor TRANSPORTER = new SpriteDescriptor(SpriteDescriptor.ANIM, 220, -TILE_SIZE, -TILE_SIZE, 3, 3);
    private static final SpriteDescriptor BASE = new SpriteDescriptor(SpriteDescriptor.ANIM, 220);
    private static final AnimationDescriptor MID_PANEL = new AnimationDescriptor(List.of(BASE.withBaseIndex(224), BASE.withBaseIndex(232), BASE.withBaseIndex(233)), 1, AnimationDescriptor.Type.LOOP);
    private static final AnimationDescriptor RIGHT_PANEL = new AnimationDescriptor(List.of(BASE.withBaseIndex(225), BASE.withBaseIndex(234), BASE.withBaseIndex(235)), 1, AnimationDescriptor.Type.LOOP);
}

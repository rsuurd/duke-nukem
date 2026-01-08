package duke.gameplay.active;

import duke.gameplay.*;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.resources.AssetManager;
import duke.sfx.Sfx;

import java.util.List;

import static duke.level.Level.TILE_SIZE;

public class SecurityCamera extends Active implements Movable, Updatable, SpriteRenderable, Shootable {
    private int index = 0;

    public SecurityCamera(int x, int y) {
        super(x, y, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public void update(GameplayContext context) {
        int distance = context.getPlayer().getCol() - getCol();

        index = Integer.signum(distance) + 1;
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return DESCRIPTORS.get(index);
    }

    @Override
    public Layer getLayer() {
        return Layer.BACKGROUND;
    }

    private static List<SpriteDescriptor> DESCRIPTORS;

    static {
        SpriteDescriptor descriptor = new SpriteDescriptor(AssetManager::getAnim, 208, 0, 0, 1, 1);

        DESCRIPTORS = List.of(descriptor, descriptor.withBaseIndex(209), descriptor.withBaseIndex(210));
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        context.getScoreManager().score(100, getX(), getY());
        context.getActiveManager().spawn(EffectsFactory.createSparks(getX(), getY()));
        context.getSoundManager().play(Sfx.SMALL_DEATH);

        deactivate();
    }
}

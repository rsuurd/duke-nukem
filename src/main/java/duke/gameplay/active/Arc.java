package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.Active;
import duke.gameplay.Damaging;
import duke.gameplay.GameplayContext;
import duke.gameplay.Updatable;
import duke.gameplay.player.Player;
import duke.gfx.*;
import duke.sfx.Sfx;
import duke.sfx.SoundManager;

import static duke.gfx.SpriteDescriptor.OBJECTS;
import static duke.level.Level.TILE_SIZE;

public class Arc extends Active implements Renderable, Damaging, Updatable {
    private Animation animation;

    private boolean connectedToPlayer;
    private int connectedOffsetLeft;
    private int connectedOffsetRight;
    private int sound;

    public Arc(int x, int y, int width) {
        this(x, y, width, new Animation(BLUE));
    }

    Arc(int x, int y, int width, Animation animation) {
        super(x, y, width, TILE_SIZE);

        this.animation = animation;
        sound = 0;
    }

    @Override
    public void update(GameplayContext context) {
        animation.tick();

        checkConnection(context.getPlayer());
        playSound(context.getSoundManager());
    }

    private void checkConnection(Player player) {
        connectedToPlayer = player.intersects(this);

        if (connectedToPlayer) {
            connectedOffsetLeft = player.getX() - getX();
            connectedOffsetRight = connectedOffsetLeft + player.getWidth();
        }
    }

    private void playSound(SoundManager soundManager) {
        if (sound == 0) {
            soundManager.play(Sfx.FORCE_FIELD);
        }

        sound = (sound + 1) % 4;
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        for (int tileX = 0; tileX < getWidth(); tileX += TILE_SIZE) {
            spriteRenderer.render(renderer, getDescriptor(tileX), screenX + tileX, screenY);
        }
    }

    private SpriteDescriptor getDescriptor(int tileX) {
        if (isConnected(tileX)) {
            animation.setAnimation(GREEN, false);
        } else {
            animation.setAnimation(BLUE, false);
        }

        return animation.getSpriteDescriptor();
    }

    private boolean isConnected(int tileX) {
        return connectedToPlayer && (tileX + TILE_SIZE) > connectedOffsetLeft && tileX < connectedOffsetRight;
    }

    private static final SpriteDescriptor DESCRIPTOR = new SpriteDescriptor(OBJECTS, 50);
    private static final AnimationDescriptor BLUE = new AnimationDescriptor(DESCRIPTOR, 4, 1);
    private static final AnimationDescriptor GREEN = new AnimationDescriptor(DESCRIPTOR.withBaseIndex(54), 4, 1);
}

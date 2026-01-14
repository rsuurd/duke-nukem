package duke.gameplay.active;

import duke.gameplay.*;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.resources.AssetManager;
import duke.sfx.Sfx;

import static duke.level.Level.TILE_SIZE;

public class Acme extends Active implements Updatable, SpriteRenderable, Shootable, Damaging {
    private Rectangle detectionArea;

    private int timer;

    public Acme(int x, int y) {
        super(x, y, TILE_SIZE * 2, TILE_SIZE);

        timer = -1;
    }

    @Override
    public void update(GameplayContext context) {
        if (detectionArea == null) {
            detectionArea = createDetectionArea(context);
        }

        if (isIdle()) {
            if (context.getPlayer().intersects(detectionArea)) {
                startShaking();
            }
        } else {
            timer++;
        }

        if (isShaking()) {
            shake();
        }

        if (isFalling()) {
            if (context.isSolid(getRow() + 1, getCol())) {
                destroy(context);
            } else {
                fall(context);
            }
        }
    }

    private Rectangle createDetectionArea(GameplayContext context) {
        int top = getY() + getHeight();

        return new Rectangle(getX(), top, getWidth(), findFloorBelow(context) - top);
    }

    private int findFloorBelow(GameplayContext context) {
        int col = getCol();

        for (int row = (getY() + getHeight()) / TILE_SIZE; row < Level.HEIGHT; row++) {
            if (context.isSolid(row, col)) {
                return row * TILE_SIZE;
            }
        }

        return Level.HEIGHT * TILE_SIZE;
    }

    boolean isIdle() {
        return timer < 0;
    }

    private void startShaking() {
        timer = 0;
    }

    boolean isShaking() {
        return !isIdle() && timer < SHAKE_TIME;
    }

    private void shake() {
        if (timer % 2 == 0) {
            setY(getY() + 1);
        } else {
            setY(getY() - 1);
        }
    }

    boolean isFalling() {
        return timer >= SHAKE_TIME;
    }

    private void fall(GameplayContext context) {
        if (timer == SHAKE_TIME) {
            context.getSoundManager().play(Sfx.DANGER_SIGN);
        }

        setY(getY() + FALL_SPEED);
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return SPRITE;
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        int effectsX = getX() + 8;

        context.getScoreManager().score(500, effectsX, getY());
        context.getActiveManager().spawn(EffectsFactory.createSparks(effectsX, getY()));
        destroy(context);
    }

    private void destroy(GameplayContext context) {
        int effectsX = getX() + 8;
        int effectsY = getY() - 8;

        context.getActiveManager().spawn(EffectsFactory.createSmoke(effectsX, effectsY));
        context.getActiveManager().spawn(EffectsFactory.createParticles(effectsX, effectsY));

        destroy();
    }

    private static final SpriteDescriptor SPRITE = new SpriteDescriptor(AssetManager::getObjects, 83, 0, 0, 1, 2);

    static final int SHAKE_TIME = 10;
    private static final int FALL_SPEED = 12;
}

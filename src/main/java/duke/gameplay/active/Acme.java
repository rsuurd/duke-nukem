package duke.gameplay.active;

import duke.gameplay.*;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderable;
import duke.level.Level;
import duke.resources.AssetManager;
import duke.sfx.Sfx;

import static duke.level.Level.TILE_SIZE;

public class Acme extends Active implements Updatable, SpriteRenderable, Shootable {
    private Rectangle detectionArea;

    private int timer;

    public Acme(int x, int y) {
        super(x, y, TILE_SIZE * 2, TILE_SIZE);

        timer = -1;
    }

    @Override
    public void update(GameplayContext context) {
        if (detectionArea == null) {
            detectionArea = createDetectionArea(context.getLevel());
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

        if (isDetaching() || isFalling()) {
            fall(context);
        }

        if (isFalling() && context.getLevel().isSolid(getRow(), getCol())) {
            crash(context);
        }
    }

    private Rectangle createDetectionArea(Level level) {
        int top = getY() + getHeight();

        return new Rectangle(getX(), top, getWidth(), findFloorBelow(level) - top);
    }

    private int findFloorBelow(Level level) {
        int col = getCol();

        for (int row = (getY() + getHeight()) / TILE_SIZE; row < Level.HEIGHT; row++) {
            if (level.isSolid(row, col)) {
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
            setY(getY() - 1);
        } else {
            setY(getY() + 1);
        }
    }

    boolean isDetaching() {
        return timer >= SHAKE_TIME && timer < DETACH_TIME;
    }

    boolean isFalling() {
        return timer >= DETACH_TIME;
    }

    private void fall(GameplayContext context) {
        if (timer == SHAKE_TIME) {
            context.getSoundManager().play(Sfx.DANGER_SIGN);
        }

        setY(getY() + FALL_SPEED);
    }

    private void crash(GameplayContext context) {
        // particles
        // smoke
        // sound?
        deactivate();
    }

    @Override
    public SpriteDescriptor getSpriteDescriptor() {
        return SPRITE;
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        context.getScoreManager().score(500, getX() + 8, getY());
        // particles
        // sound
        deactivate();
    }

    private static final SpriteDescriptor SPRITE = new SpriteDescriptor(AssetManager::getObjects, 83, 0, 0, 1, 2);

    static final int SHAKE_TIME = 10;
    static final int DETACH_TIME = SHAKE_TIME + 2;
    private static final int FALL_SPEED = 8;
}

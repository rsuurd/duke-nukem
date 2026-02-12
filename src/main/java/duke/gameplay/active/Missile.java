package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.*;
import duke.gameplay.effects.EffectsFactory;
import duke.gfx.Renderable;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderer;
import duke.level.Level;
import duke.sfx.Sfx;

import static duke.gameplay.active.Missile.State.*;
import static duke.gfx.SpriteDescriptor.OBJECTS;
import static duke.level.Level.TILE_SIZE;

public class Missile extends Active implements Updatable, Shootable, Renderable, Wakeable {
    private State state;

    private int timer;

    public Missile(int x, int y) {
        super(x, y, TILE_SIZE, 4 * TILE_SIZE);

        state = IDLE;
    }

    @Override
    public void update(GameplayContext context) {
        if (state == IDLE) return;

        if (state == IGNITION) {
            ignition(context);
        }

        if (state == LAUNCHED) {
            fly(context);
        }
    }

    private void ignition(GameplayContext context) {
        if (--timer <= 0) {
            launch(context);
        }
    }

    private void launch(GameplayContext context) {
        state = LAUNCHED;

        int bottom = getY() + getHeight();
        int row = bottom / TILE_SIZE;
        int col = getCol();
        Level level = context.getLevel();
        level.setTile(row, col, level.getTile(row + 1, col));
        context.getActiveManager().spawn(EffectsFactory.createParticles(getX(), bottom));
    }

    private void fly(GameplayContext context) {
        setY(getY() - SPEED);

        if (timer++ % 2 == 0) {
            context.getActiveManager().spawn(EffectsFactory.createRocketBurn(getX(), getY() + getHeight(), 2));
        }

        checkForImpact(context);
    }

    private void checkForImpact(GameplayContext context) {
        Level level = context.getLevel();
        int row = getRow() + 3;
        int col = getCol();

        if (context.isSolid(row, col)) {
            destroy();
            level.setTile(row, col, level.getTile(row - 1, col));
            // TODO the box above won't drop because it's out of view, we should wake it up
            context.getActiveManager().spawn(EffectsFactory.createParticles(getX(), (row - 1) * TILE_SIZE));
            context.getSoundManager().play(Sfx.BOMB_EXPLODE);
            context.getBonusTracker().trackDestroyed(BonusTracker.Type.MISSILES);
        }
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        if (state == IDLE) {
            liftOff(context);
        }
    }

    private void liftOff(GameplayContext context) {
        state = State.IGNITION;
        timer = IGNITION_DURATION;
        context.getActiveManager().spawn(EffectsFactory.createRocketIgnition(getX(), getY() + getHeight(), IGNITION_DURATION));
        context.getActiveManager().spawn(EffectsFactory.createSlowFlash(getX(), getY() + getHeight()));
        context.getSoundManager().play(Sfx.ROCKET);
        context.getScoreManager().score(200);
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        spriteRenderer.render(renderer, WARHEAD, screenX, screenY);
        spriteRenderer.render(renderer, TUBE, screenX, screenY + TILE_SIZE);
        spriteRenderer.render(renderer, TUBE, screenX, screenY + (2 * TILE_SIZE));
        spriteRenderer.render(renderer, LEFT_FIN, screenX - TILE_SIZE, screenY + (3 * TILE_SIZE));
        spriteRenderer.render(renderer, BOTTOM, screenX, screenY + (3 * TILE_SIZE));
        spriteRenderer.render(renderer, RIGHT_FIN, screenX + TILE_SIZE, screenY + (3 * TILE_SIZE));
    }

    enum State {
        IDLE, IGNITION, LAUNCHED
    }

    static final SpriteDescriptor WARHEAD = new SpriteDescriptor(OBJECTS, 11);
    static final SpriteDescriptor TUBE = new SpriteDescriptor(OBJECTS, 12);
    static final SpriteDescriptor BOTTOM = new SpriteDescriptor(OBJECTS, 13);
    static final SpriteDescriptor LEFT_FIN = new SpriteDescriptor(OBJECTS, 14);
    static final SpriteDescriptor RIGHT_FIN = new SpriteDescriptor(OBJECTS, 15);

    static final int IGNITION_DURATION = 8;
    static final int SPEED = 16;
}

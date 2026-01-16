package duke.gameplay.active;

import duke.Renderer;
import duke.gameplay.*;
import duke.gameplay.effects.EffectsFactory;
import duke.gameplay.effects.ReactorDestructionSequence;
import duke.gfx.*;
import duke.level.Level;
import duke.sfx.Sfx;

public class Reactor extends Active implements Updatable, Damaging, Renderable, Shootable {
    private Animation animation;
    private int health;

    public Reactor(int x, int y) {
        super(x, y, Level.TILE_SIZE, 3 * Level.TILE_SIZE);

        animation = new Animation(SPINNING);
        health = HEALTH;
    }

    @Override
    public void update(GameplayContext context) {
        animation.tick();
        context.getSoundManager().play(Sfx.REACTOR_SOUND);
    }

    @Override
    public int getDamage() {
        return 99;
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        SpriteDescriptor spriteDescriptor = animation.getSpriteDescriptor();

        for (int y = screenY; y < screenY + getHeight(); y += Level.TILE_SIZE) {
            spriteRenderer.render(renderer, spriteDescriptor, screenX, y);
        }
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        health--;

        context.getActiveManager().spawn(EffectsFactory.createParticles(bolt.getX(), bolt.getY()));
        context.getActiveManager().spawn(EffectsFactory.createReactorHitEffect(getX(), getY(), getHeight()));

        if (health <= 0) {
            context.getActiveManager().spawn(new ReactorDestructionSequence(getX(), getY(), getHeight(), animation.getSpriteDescriptor()));
            destroy();
        }
    }

    private static final AnimationDescriptor SPINNING = new AnimationDescriptor(new SpriteDescriptor(SpriteDescriptor.OBJECTS, 90), 5, 1);
    private static final int HEALTH = 10;
}

package duke.gameplay.effects;

import duke.Renderer;
import duke.gameplay.BonusTracker;
import duke.gfx.Renderable;
import duke.gfx.SimpleSpriteRenderable;
import duke.gfx.SpriteDescriptor;
import duke.gfx.SpriteRenderer;

import java.util.List;

import static duke.gfx.SpriteDescriptor.NUMBERS;
import static duke.level.Level.TILE_SIZE;

public class Bonus extends Effect implements Renderable {
    private List<SpriteDescriptor> tiles;

    public Bonus(int x, int y, BonusTracker.Type type) {
        super(x, y, (SimpleSpriteRenderable) null, TTL);
        tiles = create(type);
        setVelocityY(-SPEED);
    }

    @Override
    public void render(Renderer renderer, SpriteRenderer spriteRenderer, int screenX, int screenY) {
        int index = ttl % 2;

        spriteRenderer.render(renderer, tiles.get(index), screenX, screenY);
        spriteRenderer.render(renderer, tiles.get(index + 2), screenX + TILE_SIZE, screenY);
    }

    private static List<SpriteDescriptor> create(BonusTracker.Type type) {
        int baseIndex = 14 + type.ordinal() * 4;

        return List.of(
                new SpriteDescriptor(NUMBERS, baseIndex),
                new SpriteDescriptor(NUMBERS, baseIndex + 1),
                new SpriteDescriptor(NUMBERS, baseIndex + 2),
                new SpriteDescriptor(NUMBERS, baseIndex + 3)
        );
    }

    private static final int SPEED = 2;
    private static final int TTL = 200;
}

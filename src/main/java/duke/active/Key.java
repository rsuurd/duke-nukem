package duke.active;

import duke.GameState;
import duke.effects.Effect;

public class Key extends Item {
    public static final int RED_KEY_TILE_ID = 0x3044;
    public static final int GREEN_KEY_TILE_ID = 0x3045;
    public static final int BLUE_KEY_TILE_ID = 0x3046;
    public static final int MAGENTA_KEY_TILE_ID = 0x3047;

    private Type type;

    public Key(int x, int y, Type type) {
        super(x, y, 124 + type.ordinal());

        this.type = type;
    }

    @Override
    protected void applyGravity() {}

    @Override
    protected void pickedUp(GameState state) {
        state.increaseScore(1000);
        state.addEffect(new Effect.Score(x, y, 1000));

        state.getInventory().addKey(type);
    }

    public enum Type {
        RED, GREEN, BLUE, MAGENTA
    }
}

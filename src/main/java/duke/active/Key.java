package duke.active;

import duke.GameState;

public class Key extends Item {

    private Type type;

    public Key(int x, int y, Type type) {
        super(x, y, 124 + type.ordinal(), 1000);

        this.type = type;
    }

    @Override
    protected void applyGravity() {}

    @Override
    protected void pickedUp(GameState state) {
        super.pickedUp(state);

        state.getInventory().addKey(type);
    }

    public enum Type {
        RED, GREEN, BLUE, MAGENTA
    }
}

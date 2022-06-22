package duke.active;

import duke.GameState;

public class Letter extends Item {
    public Letter(int x, int y, char c) {
        super(x, y, switch (c) {
            case 'D' -> 118;
            case 'U' -> 119;
            case 'K' -> 120;
            case 'E' -> 121;
            default -> -1;
        }, 500);
    }

    @Override
    protected void pickedUp(GameState state) {
        // TODO check if all letters are picked up in order

        super.pickedUp(state);
    }
}

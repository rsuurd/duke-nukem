package duke.active;

import duke.Bonus;
import duke.GameState;

public class Letter extends Item {
    private char c;

    public Letter(int x, int y, char c) {
        super(x, y, switch (c) {
            case 'D' -> 118;
            case 'U' -> 119;
            case 'K' -> 120;
            case 'E' -> 121;
            default -> -1;
        }, 500);

        this.c = c;
    }

    @Override
    protected void pickedUp(GameState state) {
        state.getBonus().addLetter(c);

        if (state.getBonus().isEarned(Bonus.Type.DUKE)) {
            points = 10000;
        }

        super.pickedUp(state);
    }
}

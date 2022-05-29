package duke;

public class Acme extends Active {
    private static final int FALL_SPEED = 14;

    private boolean dropping;
    private int counter;

    public Acme(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(GameState state) {
        Duke duke = state.getDuke();

        if (dropping) {
            if (counter == 0) {
                // do nothing
            } else if (counter < 14) {
                y = y + (((counter % 2) == 0) ? -1 : 1);
            } else {
                y += FALL_SPEED;

                if ((counter > 16) && state.getLevel().collides(x, y, 31, 15)) {
                    crash();
                }
            }
            counter++;
        } else if (y < duke.getY() && (x < duke.getX()) && ((x + 31) > duke.getX())) {
            dropping = true;
        }
    }

    private void crash() {
        active = false;
    }
}

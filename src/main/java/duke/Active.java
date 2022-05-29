package duke;

abstract class Active {
    protected int x;
    protected int y;

    protected boolean active;

    protected Active(int x, int y) {
        this.x = x;
        this.y = y;

        active = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isActive() {
        return active;
    }

    public void update(GameState state) {}
}

package duke.gameplay;

public abstract class Active {
    protected int x, y;
    protected int width, height;

    protected Active(int x, int y, int width, int height) {
        moveTo(x, y);

        this.width = width;
        this.height = height;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

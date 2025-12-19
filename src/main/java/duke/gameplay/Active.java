package duke.gameplay;

public abstract class Active implements Movable, Collidable {
    protected int x, y;
    protected int width, height;

    protected Active(int x, int y, int width, int height) {
        setX(x);
        setY(y);

        this.width = width;
        this.height = height;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}

package duke.gameplay;

public abstract class Active implements Movable {
    private Rectangle bounds;

    private int velocityX, velocityY;
    private boolean active;

    protected Active(int x, int y, int width, int height) {
        bounds = new Rectangle(x, y, width, height);

        active = true;
    }

    @Override
    public int getX() {
        return bounds.getX();
    }

    @Override
    public void setX(int x) {
        bounds.setX(x);
    }

    @Override
    public int getY() {
        return bounds.getY();
    }

    @Override
    public void setY(int y) {
        bounds.setY(y);
    }

    @Override
    public int getWidth() {
        return bounds.getWidth();
    }

    @Override
    public int getHeight() {
        return bounds.getHeight();
    }

    @Override
    public int getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    @Override
    public int getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isActive() {
        return active;
    }

    protected void deactivate() {
        active = false;
    }

    public boolean intersects(Active other) {
        return bounds.intersects(other.bounds);
    }
}

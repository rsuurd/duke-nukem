package duke.gameplay;

import duke.level.Level;

public abstract class Active implements Movable {
    private Rectangle bounds;

    private int velocityX, velocityY;
    private boolean activated, destroyed;

    protected Active(int x, int y, int width, int height) {
        bounds = new Rectangle(x, y, width, height);

        activated = false;
        destroyed = false;
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

    public boolean isActivated() {
        return activated;
    }

    protected void activate() {
        activated = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    protected void destroy() {
        destroyed = true;
    }

    public boolean intersects(Active other) {
        return intersects(other.bounds);
    }

    public boolean intersects(Rectangle rectangle) {
        return bounds.intersects(rectangle);
    }

    public int getRow() {
        return getY() / Level.TILE_SIZE;
    }

    public int getCol() {
        return getX() / Level.TILE_SIZE;
    }
}

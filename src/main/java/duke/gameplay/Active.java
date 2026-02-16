package duke.gameplay;

import duke.level.Level;

public abstract class Active implements Movable {
    private Rectangle bounds;

    private int velocityX, velocityY;
    private int additionalVelocityX, additionalVelocityY;

    private boolean destroyed;

    protected Active(int x, int y, int width, int height) {
        bounds = new Rectangle(x, y, width, height);

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
    public void setWidth(int width) {
        bounds.setWidth(width);
    }

    @Override
    public int getHeight() {
        return bounds.getHeight();
    }

    @Override
    public void setHeight(int height) {
        bounds.setHeight(height);
    }

    @Override
    public int getVelocityX() {
        return velocityX;
    }

    @Override
    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public int getExternalVelocityX() {
        return additionalVelocityX;
    }

    public void addExternalVelocityX(int velocityX) {
        this.additionalVelocityX += velocityX;
    }

    public int getExternalVelocityY() {
        return additionalVelocityY;
    }

    public void addExternalVelocityY(int velocityY) {
        this.additionalVelocityY += velocityY;
    }

    public void resetExternalVelocity() {
        additionalVelocityX = 0;
        additionalVelocityY = 0;
    }

    @Override
    public int getVelocityY() {
        return velocityY;
    }

    @Override
    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    protected void destroy() {
        destroyed = true;
    }

    public boolean intersects(Active other) {
        return bounds.intersects(other.bounds);
    }

    public boolean intersects(Rectangle rectangle) {
        return bounds.intersects(rectangle);
    }

    public boolean intersects(int x, int y, int width, int height) {
        return this.bounds.intersects(x, y, width, height);
    }

    public int getRow() {
        return getY() / Level.TILE_SIZE;
    }

    public int getCol() {
        return getX() / Level.TILE_SIZE;
    }

    protected boolean hasOnTop(Physics body) {
        int bodyBottom = body.getY() + body.getHeight();
        int bodyRight = body.getX() + body.getWidth();

        return bodyBottom == getY() && bodyRight > getX() && body.getX() < getX() + getWidth();
    }
}

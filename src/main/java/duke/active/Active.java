package duke.active;

import duke.*;

import static duke.Gfx.TILE_SIZE;

public abstract class Active {
    protected int x;
    protected int y;

    protected int width;
    protected int height;

    protected int velocityX;
    protected int velocityY;

    protected boolean active;

    protected Active(int x, int y) {
        this.x = x;
        this.y = y;

        width = TILE_SIZE - 1;
        height = TILE_SIZE - 1;

        velocityX = 0;
        velocityY = 0;

        active = true;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void applyGravity() {
        velocityY = 8;
    }

    protected void moveHorizontally(Level level) {
        boolean collision = false;

        int newPositionX = x + velocityX;

        int row = y / TILE_SIZE;
        int col = (newPositionX + ((velocityX > 0) ? width : 0)) / TILE_SIZE;

        while (!collision && (row <= (y + height) / TILE_SIZE)) {
            collision = level.isSolid(row++, col);
        }

        if (collision) {
            hitWall();
        } else {
            moveTo(newPositionX, y);
        }

        velocityX = 0;
    }

    protected void hitWall() {}

    protected void moveVertically(GameState state) {
        boolean collision = false;
        int sign = Integer.signum(velocityY);
        int i = 0;

        while (!collision && (i < Math.abs(velocityY))) {
            int newPositionY = y + sign;

            int row = (newPositionY + ((velocityY > 0) ? height : 0)) / TILE_SIZE;
            int col = x / TILE_SIZE;

            while (!collision && (col <= (x + width) / TILE_SIZE)) {
                collision = state.getLevel().isSolid(row, col++);
            }

            if (collision) {
                if (velocityY < 0) {
                    bump();
                } else {
                    land(state);
                }

                velocityY = 0;
            } else {
                moveTo(x, newPositionY);
            }

            i++;
        }
    }

    protected void bump() {}
    protected void land(GameState state) {}

    public void update(GameState state) {
        moveHorizontally(state.getLevel());
        moveVertically(state);

        applyGravity();
    }

    public boolean collidesWith(Active active) {
        return collidesWith(active.getX(), active.getY(), active.getWidth(), active.getHeight());
    }

    public boolean collidesWith(int x, int y, int w, int h) {
        return ((this.x < (x + w)) && ((this.x + this.width) > x) &&
                (this.y < (y + h)) && ((this.y + this.height) > y));
    }

    public boolean isOnTop(Active active) {
        return (x >= active.getX()) && ((x + width) <= (active.getX() + active.getWidth())) && ((y + height + 1) == active.getY());
    }

    public boolean canBeShot() {
        return false;
    }

    public void hit(GameState state) {}

    public void render(Renderer renderer, Assets assets) {
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

    public boolean isActive() {
        return active;
    }
}

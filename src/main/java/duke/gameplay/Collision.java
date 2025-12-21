package duke.gameplay;

import duke.level.Level;

import static duke.level.Level.TILE_SIZE;

public class Collision {
    public void resolve(Movable movable, Level level) {
        resolveXAxis(movable, level);

        if (movable instanceof Physics physics) {
            applyGravity(physics, level);
        }

        resolveYAxis(movable, level);
    }

    private void applyGravity(Physics physics, Level level) {
        if (isSolidBelow(physics, level) && physics.getVelocityY() >= 0) {
            onCollision(physics, Collidable.Direction.DOWN);
        } else {
            physics.fall();
        }

        int velocityY = Math.min(physics.getVelocityY() + physics.getVerticalAcceleration(), physics.getTerminalVelocity());

        physics.setVelocityY(velocityY);
    }

    private void resolveXAxis(Movable movable, Level level) {
        int velocityX = movable.getVelocityX();
        if (velocityX == 0) return;

        int newX = movable.getX() + velocityX;
        int resolvedX = newX;

        if (collides(newX, movable.getY(), movable.getWidth(), movable.getHeight(), level)) {
            if (velocityX > 0) {
                resolvedX = snapRight(movable, newX);
            } else {
                resolvedX = snapLeft(movable, newX);
            }
        }

        movable.setX(resolvedX);
    }

    private int snapRight(Movable movable, int newX) {
        onCollision(movable, Collidable.Direction.RIGHT);

        int right = newX + movable.getWidth() - 1;
        int col = right / TILE_SIZE;

        return col * TILE_SIZE - movable.getWidth();
    }

    private int snapLeft(Movable movable, int newX) {
        onCollision(movable, Collidable.Direction.LEFT);

        int col = newX / TILE_SIZE;

        return (col + 1) * TILE_SIZE;
    }

    private void resolveYAxis(Movable movable, Level level) {
        int velocityY = movable.getVelocityY();
        if (velocityY == 0) return;

        int newY = movable.getY() + velocityY;
        int resolvedY = newY;

        if (collides(movable.getX(), newY, movable.getWidth(), movable.getHeight(), level)) {
            if (velocityY > 0) {
                resolvedY = snapToGround(movable, newY);
            } else {
                resolvedY = snapToCeiling(movable, newY);
            }
        }

        movable.setY(resolvedY);
    }

    private int snapToGround(Movable movable, int newY) {
        onCollision(movable, Collidable.Direction.DOWN);

        int bottom = newY + movable.getHeight() - 1;
        int row = bottom / TILE_SIZE;

        return row * TILE_SIZE - movable.getHeight();
    }

    private int snapToCeiling(Movable movable, int newY) {
        onCollision(movable, Collidable.Direction.UP);

        int row = newY / TILE_SIZE;

        return (row + 1) * TILE_SIZE;
    }

    private void onCollision(Movable movable, Collidable.Direction direction) {
        if (movable instanceof Collidable collidable) {
            collidable.onCollision(direction);
        }
    }

    private boolean collides(int x, int y, int width, int height, Level level) {
        int left = x / TILE_SIZE;
        int right = (x + width - 1) / TILE_SIZE;
        int top = y / TILE_SIZE;
        int bottom = (y + height - 1) / TILE_SIZE;

        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                if (level.isSolid(row, col)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isSolidBelow(Movable movable, Level level) {
        int x = movable.getX();
        int y = movable.getY() + movable.getHeight();

        int left = x / TILE_SIZE;
        int right = (x + movable.getWidth() - 1) / TILE_SIZE;
        int row = y / TILE_SIZE;

        for (int col = left; col <= right; col++) {
            if (level.isSolid(row, col)) {
                return true;
            }
        }

        return false;
    }
}

package duke.gameplay;

import duke.level.Flags;

import static duke.level.Level.TILE_SIZE;

public class Collision {
    public void resolve(Physics body, WorldQuery query) {
        resolveXAxis(body, query);
        applyGravity(body, query);
        resolveYAxis(body, query);
    }

    private void applyGravity(Physics body, WorldQuery query) {
        if (body.getVelocityY() >= 0) {
            int flagsBelow = getTileFlagsBelow(body, query);

            if (Flags.SOLID.isSet(flagsBelow)) {
                onCollision(body, Collidable.Direction.DOWN, flagsBelow);
            } else {
                body.fall();
            }
        }

        int velocityY = Math.min(body.getVelocityY() + body.getVerticalAcceleration(), body.getTerminalVelocity());

        body.setVelocityY(velocityY);
    }

    private void resolveXAxis(Physics body, WorldQuery query) {
        int velocityX = body.getVelocityX();
        if (velocityX == 0) return;

        int newX = body.getX() + velocityX;
        int resolvedX = newX;

        int flags = collides(newX, body.getY(), body.getWidth(), body.getHeight(), query);

        if (Flags.SOLID.isSet(flags)) {
            if (velocityX > 0) {
                resolvedX = snapRight(body, newX, flags);
            } else {
                resolvedX = snapLeft(body, newX, flags);
            }
        }

        body.setX(resolvedX);
    }

    private int snapRight(Physics body, int newX, int flags) {
        onCollision(body, Collidable.Direction.RIGHT, flags);

        int right = newX + body.getWidth() - 1;
        int col = right / TILE_SIZE;

        return col * TILE_SIZE - body.getWidth();
    }

    private int snapLeft(Physics body, int newX, int flags) {
        onCollision(body, Collidable.Direction.LEFT, flags);

        int col = newX / TILE_SIZE;

        return (col + 1) * TILE_SIZE;
    }

    private void resolveYAxis(Physics body, WorldQuery query) {
        int velocityY = body.getVelocityY();
        if (velocityY == 0) return;

        int newY = body.getY() + velocityY;
        int resolvedY = newY;

        int flags = collides(body.getX(), newY, body.getWidth(), body.getHeight(), query);

        if (Flags.SOLID.isSet(flags)) {
            if (velocityY > 0) {
                resolvedY = snapToGround(body, newY, flags);
            } else {
                resolvedY = snapToCeiling(body, newY, flags);
            }
        }

        body.setY(resolvedY);
    }

    private int snapToGround(Physics body, int newY, int flags) {
        onCollision(body, Collidable.Direction.DOWN, flags);

        int bottom = newY + body.getHeight() - 1;
        int row = bottom / TILE_SIZE;

        return row * TILE_SIZE - body.getHeight();
    }

    private int snapToCeiling(Physics body, int newY, int flags) {
        onCollision(body, Collidable.Direction.UP, flags);

        int row = newY / TILE_SIZE;

        return (row + 1) * TILE_SIZE;
    }

    private void onCollision(Physics body, Collidable.Direction direction, int flags) {
        if (body instanceof Collidable collidable) {
            collidable.onCollision(direction, flags);
        }
    }

    private int collides(int x, int y, int width, int height, WorldQuery query) {
        int flags = 0;

        int left = x / TILE_SIZE;
        int right = (x + width - 1) / TILE_SIZE;
        int top = y / TILE_SIZE;
        int bottom = (y + height - 1) / TILE_SIZE;

        for (int row = top; row <= bottom; row++) {
            for (int col = left; col <= right; col++) {
                // TODO we still do query.isSolid because we want to consider solid actives as well
                // maybe we can also use the SOLID flag for tiles occupied by solid actives. For now double check is ok
                if (query.isSolid(row, col)) {
                    // exit early (maybe too early?)
                    return query.getTileFlags(row, col);
                }
            }
        }

        return flags;
    }

    private int getTileFlagsBelow(Physics body, WorldQuery query) {
        int x = body.getX();
        int y = body.getY() + body.getHeight();

        return collides(x, y, body.getWidth(), 1, query);
    }
}

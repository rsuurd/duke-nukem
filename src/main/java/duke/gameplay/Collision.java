package duke.gameplay;

import duke.level.Level;

import static duke.level.Level.TILE_SIZE;

public class Collision {
    public void resolve(Player player, Level level) {
        applyGravity(player, level);
        resolveXAxis(player, level);
        resolveYAxis(player, level);
    }

    private void applyGravity(Player player, Level level) {
        int strength = 0;

        if (player.getState() == Player.State.JUMPING) {
            strength = GRAVITY;
        } else if (!isSolidBelow(player, level)) {
            player.fall();
            strength = Player.SPEED;
        }

        if (strength != 0) {
            player.setVelocity(player.getVelocityX(), Math.min(player.getVelocityY() + strength, TILE_SIZE));
        }
    }

    private void resolveXAxis(Player player, Level level) {
        int velocityX = player.getVelocityX();
        if (velocityX == 0) return;

        int newX = player.getX() + velocityX;
        int resolvedX = newX;

        if (collides(newX, player.getY(), player.getWidth(), player.getHeight(), level)) {
            Collidable.Direction direction;

            if (velocityX > 0) {
                direction = Collidable.Direction.RIGHT;
                int right = newX + player.getWidth() - 1;
                int col = right / TILE_SIZE;
                resolvedX = col * TILE_SIZE - player.getWidth();
            } else {
                direction = Collidable.Direction.LEFT;
                int col = newX / TILE_SIZE;
                resolvedX = (col + 1) * TILE_SIZE;
            }

            player.onCollide(direction);
        }

        player.moveTo(resolvedX, player.getY());
        player.setVelocity(0, player.getVelocityY());
    }

    private void resolveYAxis(Player player, Level level) {
        int velocityY = player.getVelocityY();
        if (velocityY == 0) return;

        int newY = player.getY() + velocityY;
        int resolvedY = newY;

        if (collides(player.getX(), newY, player.getWidth(), player.getHeight(), level)) {
            Collidable.Direction direction;

            if (velocityY > 0) {
                resolvedY = snapToGround(player, newY);
                direction = Collidable.Direction.DOWN;
            } else {
                resolvedY = snapToCeiling(newY);
                direction = Collidable.Direction.UP;
            }

            player.onCollide(direction);
        }

        player.moveTo(player.getX(), resolvedY);
    }

    private int snapToGround(Player player, int newY) {
        int bottom = newY + player.getHeight() - 1;
        int row = bottom / TILE_SIZE;

        return row * TILE_SIZE - player.getHeight();
    }

    private int snapToCeiling(int newY) {
        int row = newY / TILE_SIZE;

        return (row + 1) * TILE_SIZE;
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

    private boolean isSolidBelow(Player player, Level level) {
        int x = player.getX() + player.getVelocityX();
        int y = player.getY() + player.getHeight();

        int left = x / TILE_SIZE;
        int right = (x + player.getWidth() - 1) / TILE_SIZE;
        int row = y / TILE_SIZE;

        for (int col = left; col <= right; col++) {
            if (level.isSolid(row, col)) {
                return true;
            }
        }

        return false;
    }

    private static final int GRAVITY = 2;
}

package duke.gameplay;

import duke.level.Level;

import static duke.level.Level.TILE_SIZE;

public class Collision {
    public void resolve(Player player, Level level) {
        resolveXAxis(player, level);
        resolveYAxis(player, level);
    }

    private void resolveXAxis(Player player, Level level) {
        int velocityX = player.getVelocityX();
        if (velocityX == 0) return;

        int newX = player.getX() + velocityX;

        if (collides(newX, player.getY(), player.getWidth(), player.getHeight(), level)) {
            if (velocityX > 0) {
                int right = newX + player.getWidth() - 1;
                int col = right / TILE_SIZE;
                int worldX = col * TILE_SIZE - player.getWidth();
                player.moveTo(worldX, player.getY());
            } else {
                int col = newX / TILE_SIZE;
                int worldX = (col + 1) * TILE_SIZE;
                player.moveTo(worldX, player.getY());
            }
        } else {
            player.moveTo(newX, player.getY());
        }

        player.setVelocity(0, player.getVelocityY());
    }

    private void resolveYAxis(Player player, Level level) {
        int velocityY = player.getVelocityY();
        if (velocityY == 0) return;

        player.setGrounded(false);

        int newY = player.getY() + velocityY;

        if (collides(player.getX(), newY, player.getWidth(), player.getHeight(), level)) {
            if (velocityY > 0) {
                int bottom = newY + player.getHeight();
                int row = bottom / TILE_SIZE;

                player.moveTo(player.getX(), row * TILE_SIZE - player.getHeight());
                player.setGrounded(true);
            } else {
                int row = newY / TILE_SIZE;

                player.moveTo(player.getX(), (row + 1) * TILE_SIZE);
            }
        } else {
            player.moveTo(player.getX(), newY);
        }

        player.setVelocity(player.getVelocityX(), 0);
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
}

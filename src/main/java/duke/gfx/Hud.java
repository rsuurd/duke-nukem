package duke.gfx;

import duke.Renderer;
import duke.gameplay.active.items.Key;
import duke.gameplay.player.Inventory;
import duke.gameplay.player.Player;
import duke.resources.AssetManager;

import static duke.level.Level.TILE_SIZE;

public class Hud {
    private AssetManager assets;
    private Font font;

    private int frame;

    public Hud(AssetManager assets, Font font) {
        this.assets = assets;
        this.font = font;
    }

    public void render(Renderer renderer, int score, Player player, String debug) {
        drawBorders(renderer);
        drawScore(renderer, score);
        drawHealth(renderer, player.getHealth().getCurrent());
        drawFirePower(renderer, player.getWeapon().getFirepower());
        drawInventory(renderer, player.getInventory());
        drawHelp(renderer);
        drawDebugInfo(renderer, debug);

        frame = (frame + 1) % 2;
    }

    private void drawBorders(Renderer renderer) {
        renderer.draw(assets.getBorder().get(4), 0, 0);
        renderer.draw(assets.getBorder().get(7), 0, 176);
        renderer.draw(assets.getBorder().get(9), 304, 0);
        renderer.draw(assets.getBorder().get(11), 304, 176);
        renderer.draw(assets.getBorder().get(5), 224, 0);
        renderer.draw(assets.getBorder().get(6), 224, 176);

        for (int i = 1; i < 14; i++) {
            renderer.draw(assets.getBorder().get(2), i * TILE_SIZE, 0);
            renderer.draw(assets.getBorder().get(3), i * TILE_SIZE, 176);

            if (i < 11) {
                renderer.draw(assets.getBorder().get(0), 0, i * TILE_SIZE);
                renderer.draw(assets.getBorder().get(1), 224, i * TILE_SIZE);
                renderer.draw(assets.getBorder().get(10), 304, i * TILE_SIZE);
            }
        }
    }

    private void drawScore(Renderer renderer, int score) {
        renderer.draw(assets.getBorder().get(8), 240, 0);
        renderer.draw(assets.getBorder().get(38), 256, 0);
        renderer.draw(assets.getBorder().get(39), 272, 0);
        renderer.draw(assets.getBorder().get(8), 288, 0);

        font.drawText(renderer, String.format("%08d", score), 240, 24);
    }

    private void drawHealth(Renderer renderer, int health) {
        renderer.draw(assets.getBorder().get(14), 224, 40);
        renderer.draw(assets.getBorder().get(8), 240, 40);

        int healthLabelIndex = ((health == 1) && (frame == 0)) ? 42 : 36;
        renderer.draw(assets.getBorder().get(healthLabelIndex), 256, 40);
        renderer.draw(assets.getBorder().get(healthLabelIndex + 1), 272, 40);

        renderer.draw(assets.getBorder().get(8), 288, 40);
        renderer.draw(assets.getBorder().get(15), 304, 40);

        for (int i = 0; i < health - 1; i++) {
            renderer.draw(assets.getObjects().get(61), 240 + (i * 8), 60);
        }
    }

    private void drawFirePower(Renderer renderer, int firePower) {
        renderer.draw(assets.getBorder().get(14), 224, 80);
        renderer.draw(assets.getBorder().get(8), 240, 80);
        renderer.draw(assets.getBorder().get(8), 288, 80);
        renderer.draw(assets.getBorder().get(33), 248, 80);
        renderer.draw(assets.getBorder().get(34), 264, 80);
        renderer.draw(assets.getBorder().get(35), 280, 80);
        renderer.draw(assets.getBorder().get(15), 304, 80);
        renderer.draw(assets.getObjects().get(43), 264, 94); // gun

        for (int i = 0; i < firePower; i++) {
            renderer.draw(assets.getObjects().get(6 + i), 240 + (i * TILE_SIZE), 106);
        }
    }

    private void drawInventory(Renderer renderer, Inventory inventory) {
        renderer.draw(assets.getBorder().get(14), 224, 128);
        renderer.draw(assets.getBorder().get(8), 240, 128);
        renderer.draw(assets.getBorder().get(8), 288, 128);
        renderer.draw(assets.getBorder().get(30), 248, 128);
        renderer.draw(assets.getBorder().get(31), 264, 128);
        renderer.draw(assets.getBorder().get(32), 280, 128);
        renderer.draw(assets.getBorder().get(15), 304, 128);
        renderer.draw(assets.getBorder().get(8), 240, 176);
        renderer.draw(assets.getBorder().get(8), 256, 176);
        renderer.draw(assets.getBorder().get(8), 272, 176);
        renderer.draw(assets.getBorder().get(8), 288, 176);

        for (Key.Type key : Key.Type.values()) {
            if (inventory.hasKey(key)) {
                renderer.draw(assets.getObjects().get(124 + key.ordinal()), 240 + (key.ordinal() * TILE_SIZE), 144);
            }
        }
    }

    private void drawHelp(Renderer renderer) {
        int screenX = 88;

        for (int tile = 26; tile <= 29; tile++) {
            renderer.draw(assets.getBorder().get(tile), screenX += TILE_SIZE, 176);
        }
    }

    private void drawDebugInfo(Renderer renderer, String debug) {
        if (debug == null || debug.isEmpty()) return;

        font.drawText(renderer, debug, 16, 16);
    }
}

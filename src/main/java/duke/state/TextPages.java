package duke.state;

import duke.DukeNukemException;
import duke.GameSystems;
import duke.Renderer;
import duke.gfx.Font;
import duke.gfx.Sprite;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TextPages implements GameState {
    private String name;

    private Font font;

    private Sprite redBricks;
    private List<Sprite> cursor;

    private int tick;
    private int page;

    private List<String> pages;

    public TextPages(String name) {
        this.name = name;
    }

    @Override
    public void start(GameSystems systems) {
        pages = loadPages("/%s.txt".formatted(name));

        font = new Font(systems.getAssets());
        redBricks = systems.getAssets().getTiles().get(184);
        cursor = systems.getAssets().getObjects().subList(85, 89);

        tick = 0;
        page = 0;
    }

    private List<String> loadPages(String name) {
        try (InputStream in = getClass().getResourceAsStream(name)) {
            String text = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            return List.of(text.split("\n---\n"));
        } catch (Exception e) {
            throw new DukeNukemException("Could not read text file: " + name, e);
        }
    }

    @Override
    public void update(GameSystems systems) {
        tick = (tick + 1) % 4;

        if (systems.getKeyHandler().consumeAny()) {
            if (page < pages.size() - 1) {
                page++;
            } else {
                systems.getStateRequester().requestState(new TitleScreen());
            }
        }
    }

    @Override
    public void render(GameSystems systems) {
        Renderer renderer = systems.getRenderer();

        drawBackground(renderer);
        drawText(renderer);
    }

    private void drawBackground(Renderer renderer) {
        for (int x = 0; x < 320; x += redBricks.getWidth()) {
            for (int y = 0; y < 200; y += redBricks.getHeight()) {
                renderer.draw(redBricks, x, y);
            }
        }
    }

    private void drawText(Renderer renderer) {
        font.drawText(renderer, pages.get(page), 0, 0);

        renderer.draw(cursor.get(tick), 312, 192);
    }
}

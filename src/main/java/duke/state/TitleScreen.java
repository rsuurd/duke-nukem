package duke.state;

import duke.GameSystems;
import duke.Renderer;
import duke.gfx.Sprite;
import duke.menu.Confirmation;
import duke.menu.MainMenu;
import duke.menu.Menu;
import duke.menu.MenuManager;
import duke.ui.KeyHandler;

import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_Q;

public class TitleScreen implements GameState {
    private Sprite background;
    private Menu menu;

    @Override
    public void start(GameSystems systems) {
        background = systems.getAssets().getImage("DN");
        menu = new MainMenu();

        systems.getPalette().fadeIn();
    }

    @Override
    public void update(GameSystems systems) {
        KeyHandler handler = systems.getKeyHandler();
        MenuManager menuManager = systems.getMenuManager();

        menuManager.update(systems);

        if (handler.consume(VK_ESCAPE) || handler.consume(VK_Q)) {
            confirmQuit(systems);
        }

        if (!menuManager.isOpen() && handler.consumeAny()) {
            menuManager.open(menu, systems);
        }
    }

    private void confirmQuit(GameSystems systems) {
        systems.getMenuManager().open(new Confirmation(56, 80, "Are you sure you want to\n         quit?", () -> System.exit(0)), systems);
    }

    @Override
    public void render(GameSystems systems) {
        Renderer renderer = systems.getRenderer();

        renderer.draw(background, 0, 0);

        systems.getDialogManager().render(systems.getRenderer());
    }
}
